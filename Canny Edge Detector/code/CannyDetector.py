from typing import overload
import numpy as np
import math
import matplotlib.pyplot as plt
from numpy.core.fromnumeric import ndim
from scipy import ndimage

class CannyDetector:
    def __init__(self):
        pass

    def GenerateGaussianMask(self, sigma, verbose=1):
        """Generates a gaussian mask given the input sigma

        Args:
            sigma (double): The standard deviation of the gaussian function to generate the mask with
            verbose (int): Displays graph of 2D kernel using the 1D mask. Defaults to 1

        Returns:
            G (numpy array): 1D gaussian mask
            size (int): size of the kernel
        """

        # Use input sigma to get an appropriate size for the kernels
        radius = round((2.5 * sigma) - 0.5)
        size = (2 * radius) + 1

        G = np.zeros(size)
        sumGauss = 0
        denom = (2 * sigma * sigma)

        # Sample gaussian values for each i in the kernel
        for i in range(size):
            num = -(i - radius) * (i - radius)
            G[i] = np.exp(num / denom)
            sumGauss = sumGauss + G[i]

        # Average the values
        for i in range(size):
            G[i] = G[i] / sumGauss

        # Display 2D kernel using 1D kernel
        if verbose == 1:
            G_2D = np.outer(G.T, G.T)
            plt.imshow(G_2D, interpolation='none',cmap='gray')
            plt.title("Gaussian Filter")
            plt.show()

        return G, size

    def GenerateGaussianDerivative(self, sigma, verbose = 1):
        """Gets the derivative of the Gaussian using the input sigma

        Args:
            sigma (double): The standard deviation of the gaussian function to generate the mask with
            verbose (int): Displays graph of 2D kernel using the 1D mask. Defaults to 1.

        Returns:
            G_prime (numpy array): 1D gaussian derivative mask
        """

        # Use input sigma to get an appropriate size for the kernels
        radius = round((2.5 * sigma) - 0.5)
        size = (2 * radius) + 1

        G_prime = np.zeros(size)
        sumGauss = 0
        denom = (2 * np.power(sigma, 2))

        # Sample gaussian derivative values for each i in the kernel
        for i in range(size):
            num = -(i - radius) * (i - radius)
            G_prime[i] = (-(i - radius) / (sigma * sigma)) * np.exp(num / denom)
            sumGauss = sumGauss + (i * G_prime[i])

        # Average the values
        for i in range(size):
            G_prime[i] = G_prime[i] / sumGauss

        # Display graph of d/dxdy of the gaussian kernel
        if verbose == 1:
            G_prime_2D = np.outer(G_prime, G_prime.T)
            plt.imshow(G_prime_2D, interpolation='none',cmap='gray')
            plt.title("d/dxdy of Gaussian Filter")
            plt.show()
        
        return G_prime

    def Convolve_Prime(self, Ix, Iy, G_prime, size=3):
        """Convolves the x Image blur and the y Image blur with a gaussian derivative kernel

        Args:
            Ix (numpy array): The x-direction blur of the Image
            Iy (numpy array): The y-direction blur of the Image
            G_prime (numpy array): The 1D gaussian derivative kernel
            size (int, optional): [description]. Defaults to 3.

        Returns:
            Ixx (numpy array): The x-direction derivative of the image
            Iyy (numpy array): The y-direction derivative of the image
        """
        Ixx = np.zeros((Ix.shape[0], Ix.shape[1]))
        Iyy = np.zeros((Iy.shape[0], Iy.shape[1]))

        # Horizontal Convolution
        y, x = Ix.shape
        x = x - size + 1
        for i in range(y):
            for j in range(x):
                weights = Iy[i, j:j+size]*G_prime
                Ixx[i, j] = np.sum(weights)

        # Vertical Convolution
        y, x = Iy.shape
        y = y - size + 1
        for i in range(y):
            for j in range(x):
                weights = Ix[i:i+size, j]*G_prime
                Iyy[i, j] = np.sum(weights)
        
        return Ixx, Iyy

    def Convolve(self, I, G, size=3):
        """Convolves a gaussian mask with an image in both the x and y directions

        Args:
            I (numpy array): The image matrix to convolve
            G (numpy array): The gaussian mask to convolve with
        """
        Ix = np.zeros((I.shape[0], I.shape[1]))
        Iy = np.zeros((I.shape[0], I.shape[1]))

        # Horizontal Conv
        y, x = I.shape
        x = x - size + 1
        for i in range(y):
            for j in range(x):
                weights = I[i, j:j+size]*G
                Ix[i, j] = np.sum(weights)

        y, x = I.shape
        y = y - size + 1
        
        # Vertical Conv
        for i in range(y):
            for j in range(x):
                vals = I[i:i+size, j]
                weights = I[i:i+size, j]*G
                Iy[i, j] = np.sum(weights)
        
        return Ix, Iy

    def ComputeMagnitude(self, Ixx, Iyy):
        """Computes the magnitude and phase (theta) of an image using the x and y derivatives

        Args:
            Ixx (numpy array): The x-direction derivative of the image
            Iyy (numpy array): The y-direction derivative of the image

        Returns:
            M (numpy array): The magnitude of the image
            P (numpy array): The phase (theta) of the image
        """

        M = np.hypot(Ixx, Iyy)
        P = np.arctan2(Iyy, Ixx) * 180 / math.pi
        return M, P

    def NonMaxSuppression(self, M, P):
        """Suppresses non-maximal values given input phase (theta) and magnitude of an image

        Args:
            M (numpy array): The magnitude of the image
            P (numpy array): The phase (theta) of the image

        Returns:
            NmS (numpy array): The non-maximum suppression image
        """
        NmS = np.copy(M)

        y,x = M.shape
        for i in range(1, y-1):
            for j in range(1, x-1):
                # Get the magnitude and gradient direction at the point
                mag = M[i,j]
                theta = P[i,j]

                # Determine suppression
                if (0 <= theta < 22.5 or 157.5 <= theta <= 180) and \
                (mag < M[i, j + 1] or mag < M[i, j - 1]):
                    NmS[i,j] = 0
                elif (22.5 <= theta < 67.5) and \
                    (mag < M[i-1,j+1] or mag < M[i+1,j-1]):
                    NmS[i,j] = 0
                elif (67.5 <= theta < 112.5) and \
                    (mag < M[i-1, j] or mag < M[i+1, j]):
                    NmS[i,j] = 0
                elif (112.5 <= theta < 157.5) and \
                    (mag < M[i-1, j-1] or mag < M[i+1,j+1]):
                    NmS[i,j] = 0

        return NmS

    def HysteresisThresholding(self, NMS, lower_th_ratio, upper_th_ratio):
        """Compute threshold and hysteresis given input non-maximum suppression image and lower and upper threshold ratios

        Args:
            NMS (numpy array): The non-maximum suppression image
            lower_th_ratio (double): Lower threshold ratio
            upper_th_ratio (double): Upper threshold ratio

        Returns:
            Edges (numpy array): The canny-edge image
        """

        # Calculate the upper threshold using the upper threshold ratio
        upper_th = np.max(np.max(NMS)) * upper_th_ratio

        # Calc lower threshold
        lower_th = upper_th * lower_th_ratio

        Edges = np.zeros((NMS.shape[0], NMS.shape[1]))
        y,x = NMS.shape

        # Get indices of strong and weak edges
        strong_edge_i, strong_edge_j = np.where(NMS >= upper_th)
        weak_edge_i, weak_edge_j = np.where((NMS <= upper_th) & (NMS >= lower_th))

        # Set the values
        Edges[strong_edge_i, strong_edge_j] = 255
        Edges[weak_edge_i, weak_edge_j] = 75

        # Compute hysteresis connected edges
        for i in range(1, y-1):
            for j in range(1, x-1):
                if Edges[i,j] == 75:
                    if Edges[i+1, j-1] == 255 or Edges[i+1, j] == 255 or Edges[i+1, j+1] == 255 or \
                    Edges[i, j-1] == 255 or Edges[i, j+1] == 255 or Edges[i-1, j-1] == 255 or \
                    Edges[i-1, j] == 255 or Edges[i-1, j+1] == 255:
                        Edges[i, j] = 255
                    else:
                        Edges[i, j] = 0

        return Edges