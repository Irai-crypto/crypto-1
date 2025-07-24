package com.example.todo;

/**
 * Hello world!
 *
 */
import java.util.Scanner;

public class App {
    static int[] A, M, Q, Q_1;
    static int n;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of bits: ");
        n = sc.nextInt();

        A = new int[n];
        M = new int[n];
        Q = new int[n];
        Q_1 = new int[1];

        System.out.print("Enter multiplicand (M): ");
        int m = sc.nextInt();

        System.out.print("Enter multiplier (Q): ");
        int q = sc.nextInt();

        toBinary(M, m);
        toBinary(Q, q);
        int count = n;

        System.out.println("\nInitial values:");
        printState(count);

        while (count > 0) {
            if (Q[n - 1] == 1 && Q_1[0] == 0) {
                subtract();
            } else if (Q[n - 1] == 0 && Q_1[0] == 1) {
                add();
            }

            rightShift();
            count--;

            System.out.println("\nAfter step:");
            printState(count);
        }

        System.out.println("\nFinal binary result:");
        printResult();
        sc.close();
    }

    static void toBinary(int[] bin, int num) {
        int k = bin.length - 1;
        boolean negative = num < 0;
        num = Math.abs(num);
        while (num > 0 && k >= 0) {
            bin[k--] = num % 2;
            num /= 2;
        }
        if (negative) {
            twosComplement(bin);
        }
    }

    static void twosComplement(int[] bin) {
        // 1's complement
        for (int i = 0; i < bin.length; i++) {
            bin[i] = bin[i] == 0 ? 1 : 0;
        }
        // Add 1
        int carry = 1;
        for (int i = bin.length - 1; i >= 0; i--) {
            int sum = bin[i] + carry;
            bin[i] = sum % 2;
            carry = sum / 2;
        }
    }

    static void add() {
        int carry = 0;
        for (int i = n - 1; i >= 0; i--) {
            int sum = A[i] + M[i] + carry;
            A[i] = sum % 2;
            carry = sum / 2;
        }
        System.out.println("A = A + M");
    }

    static void subtract() {
        int[] negM = M.clone();
        twosComplement(negM);
        int carry = 0;
        for (int i = n - 1; i >= 0; i--) {
            int sum = A[i] + negM[i] + carry;
            A[i] = sum % 2;
            carry = sum / 2;
        }
        System.out.println("A = A - M");
    }

    static void rightShift() {
        Q_1[0] = Q[n - 1];
        for (int i = n - 1; i > 0; i--) {
            Q[i] = Q[i - 1];
        }
        Q[0] = A[n - 1];

        for (int i = n - 1; i > 0; i--) {
            A[i] = A[i - 1];
        }
        // Sign extend A
        // A[0] remains unchanged (sign bit)
    }

    static void printState(int count) {
        System.out.print("A: ");
        printArray(A);
        System.out.print(" Q: ");
        printArray(Q);
        System.out.print(" Q-1: " + Q_1[0]);
        System.out.print("  Count: " + count);
        System.out.println();
    }

    static void printArray(int[] arr) {
        for (int bit : arr) {
            System.out.print(bit);
        }
    }

    static void printResult() {
        int[] result = new int[2 * n];
        System.arraycopy(A, 0, result, 0, n);
        System.arraycopy(Q, 0, result, n, n);
        System.out.print("Binary: ");
        printArray(result);

        // Convert binary to decimal
        int value = 0;
        boolean negative = result[0] == 1;

        if (negative) {
            twosComplement(result);
        }

        for (int i = 0; i < result.length; i++) {
            value = value * 2 + result[i];
        }

        if (negative) value = -value;

        System.out.println("\nDecimal: " + value);
    }
}
