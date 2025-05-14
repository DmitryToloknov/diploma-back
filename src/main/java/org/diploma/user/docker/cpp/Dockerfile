FROM gcc:13

WORKDIR /sandbox

ENTRYPOINT bash -c "g++ -O2 -std=c++17 main.cpp -o main.out && ./main.out < input.txt"