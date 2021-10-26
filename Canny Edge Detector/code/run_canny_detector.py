import cv2

from CannyDetector import CannyDetector
if __name__ == "__main__":
    # Params
    sigma = 1 # Sigma between 0.59 and 1 seems to work best
    im_name = '160068'
    lower_th_ratio = 0.15 # 0.15 works relatively well here
    upper_th_ratio = 0.2 # 0.2 works relatively well here

    # Read image
    I = cv2.imread(im_name + ".jpg", cv2.IMREAD_GRAYSCALE)

    # Initialize
    detector = CannyDetector()

    # Get gaussian mask
    G, size = detector.GenerateGaussianMask(sigma)

    # Convovle image with gaussian and save
    Ix, Iy = detector.Convolve(I, G, size=size)
    cv2.imwrite(im_name + '_Ix.jpg', Ix)
    cv2.imwrite(im_name + '_Iy.jpg', Iy)

    # Get gaussian derivative mask
    G_prime = detector.GenerateGaussianDerivative(sigma)

    # Convovle x and y direction image blurs with gaussian derivative and save
    Ixx, Iyy = detector.Convolve_Prime(Ix, Iy, G_prime, size=size)
    cv2.imwrite(im_name + '_Ixx.jpg', Ixx)
    cv2.imwrite(im_name + '_Iyy.jpg', Iyy)

    # Get magnitude and gradient directions and save magnitude
    M, P = detector.ComputeMagnitude(Ixx, Iyy)
    cv2.imwrite(im_name + '_mag.jpg', M)

    # Perform non-max suppression and save
    NmS = detector.NonMaxSuppression(M, P)
    cv2.imwrite(im_name + '_nms.jpg', NmS)

    # Perform hysteresis thresholding and save
    Edges = detector.HysteresisThresholding(NmS, lower_th_ratio, upper_th_ratio)
    cv2.imwrite(im_name + '_edges.jpg', Edges)