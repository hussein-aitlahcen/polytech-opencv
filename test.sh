rm ./output/test*.jpg
./seuilGris.sh 70 ./input/test.jpg ./output/test_grayscale.jpg
./sobel.sh ./output/test_grayscale.jpg ./output/test_grayscale_sobel.jpg
./evaluation.sh ./input/test_truth.bmp ./output/test_grayscale_sobel.jpg