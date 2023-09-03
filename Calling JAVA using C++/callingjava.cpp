#include <iostream>
#include <cstdlib>
using namespace std;

int main() {

    cout << "Calling the JAVA Program: javasend" << endl;
    
    // Define the command to run the Java program with the classpath
    const char* javaCommand = "java -cp /home/pratap/kafka_2.13-3.5.1/libs/*:. javasend";
    
    // Using the system function to run the Java program
    int result = system(javaCommand);
    
    if (result == 0) {
        // The Java program ran successfully
        return 0;
    } else {
        // There was an error running the Java program
        return 1;
    }
}

