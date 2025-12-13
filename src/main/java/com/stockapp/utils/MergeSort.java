package com.stockapp.utils;

import com.stockapp.models.Stock;
import java.util.ArrayList;
import java.util.List;

public class MergeSort {

    public static void sort(List<Stock> stocks) {
        if (stocks.size() <= 1) {
            return;
        }

        int mid = stocks.size() / 2;
        List<Stock> left = new ArrayList<>(stocks.subList(0, mid));
        List<Stock> right = new ArrayList<>(stocks.subList(mid, stocks.size()));

        sort(left);
        sort(right);

        merge(stocks, left, right);
    }

    private static void merge(List<Stock> result, List<Stock> left, List<Stock> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            // Sort by Ticker Symbol alphabetically
            if (left.get(i).getTicker().compareToIgnoreCase(right.get(j).getTicker()) <= 0) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }
}
