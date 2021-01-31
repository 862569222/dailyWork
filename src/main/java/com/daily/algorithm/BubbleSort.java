package com.daily.algorithm;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/1/31 22:12
 */
public class BubbleSort {

    public static void sort(int[] arr){
        if(arr == null || arr.length<2){
            return;
        }

        for (int i = arr.length-1; i >0 ; i--) {
            for (int j =0; j <i ; j++) {
                if(arr[j]> arr[j + 1]){
                    swap(arr, j, j+1);
                }
            }
        }

    }

    private static void swap(int[] arr, int i, int j) {
        /*int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;*/
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    public static void comporator(int[] arr){
        Arrays.sort(arr);
    }

    public static int[] getRandomArray(int maxSize,int maxValue){
        int[] arr = new int[(int) ((maxSize+1)* Math.random())];
        for (int i = 0; i <arr.length ; i++) {
            arr[i] = (int) ((maxValue+1)* Math.random());
        }
        return arr;
    }
    public static  int[] copyArray(int[] arr){
        if(arr == null){
            return null;
        }
        int[] copyArr = new int[arr.length];
        for (int i = 0; i <arr.length ; i++) {
            copyArr[i] = arr[i];
        }

        return copyArr;
    }

    public static boolean isEqual(int[] a ,int[] b){
        if(a == null && b == null){
            return true;
        }

        if((a == null && b != null ) || (a != null && b == null)){
            return false;
        }
        if(a.length != b.length){
            return false;
        }

        for (int i = 0; i <a.length ; i++) {
            if(a[i] != b[i]){
                return  false;
            }
        }
        return  true;
    }

    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        int times = 500000;
        int maxVal = 100;
        int maxSize = 100;
        boolean succeed = true;
        for (int i = 0; i <times ; i++) {
            int[] array = getRandomArray(maxSize, maxVal);
            int[] copyArray = copyArray(array);
            sort(array);
            comporator(copyArray);
            if(!isEqual(array,copyArray)){
                succeed = false;
                break;
            }
        }
        System.out.println(succeed?"SUCCESS":"FALIE");

        int[] arr = getRandomArray(maxSize, maxVal);
        printArray(arr);
        sort(arr);
        printArray(arr);
    }
}
