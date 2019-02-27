package com.bulygin.nikita.healthapp.ui;

import android.text.Editable;
import android.text.InputFilter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.stream.IntStream;


public class StringEditable implements Editable {

    public StringEditable(String v) {
        this.value = v;
    }

    @Override
    public int length() {
        return value.length();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    public int codePointAt(int index) {
        return value.codePointAt(index);
    }

    public int codePointBefore(int index) {
        return value.codePointBefore(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return value.codePointCount(beginIndex, endIndex);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return value.offsetByCodePoints(index, codePointOffset);
    }

    @Override
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        value.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    @Deprecated
    public void getBytes(int srcBegin, int srcEnd, byte[] dst, int dstBegin) {
        value.getBytes(srcBegin, srcEnd, dst, dstBegin);
    }

    public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
        return value.getBytes(charsetName);
    }

    public byte[] getBytes(Charset charset) {
        return value.getBytes(charset);
    }

    public byte[] getBytes() {
        return value.getBytes();
    }

    @Override
    public boolean equals(Object anObject) {
        return value.equals(anObject);
    }

    public boolean contentEquals(StringBuffer sb) {
        return value.contentEquals(sb);
    }

    public boolean contentEquals(CharSequence cs) {
        return value.contentEquals(cs);
    }

    public boolean equalsIgnoreCase(String anotherString) {
        return value.equalsIgnoreCase(anotherString);
    }

    public int compareTo(String anotherString) {
        return value.compareTo(anotherString);
    }

    public int compareToIgnoreCase(String str) {
        return value.compareToIgnoreCase(str);
    }

    public boolean regionMatches(int toffset, String other, int ooffset, int len) {
        return value.regionMatches(toffset, other, ooffset, len);
    }

    public boolean regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len) {
        return value.regionMatches(ignoreCase, toffset, other, ooffset, len);
    }

    public boolean startsWith(String prefix, int toffset) {
        return value.startsWith(prefix, toffset);
    }

    public boolean startsWith(String prefix) {
        return value.startsWith(prefix);
    }

    public boolean endsWith(String suffix) {
        return value.endsWith(suffix);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public int indexOf(int ch) {
        return value.indexOf(ch);
    }

    public int indexOf(int ch, int fromIndex) {
        return value.indexOf(ch, fromIndex);
    }

    public int lastIndexOf(int ch) {
        return value.lastIndexOf(ch);
    }

    public int lastIndexOf(int ch, int fromIndex) {
        return value.lastIndexOf(ch, fromIndex);
    }

    public int indexOf(String str) {
        return value.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return value.indexOf(str, fromIndex);
    }

    public int lastIndexOf(String str) {
        return value.lastIndexOf(str);
    }

    public int lastIndexOf(String str, int fromIndex) {
        return value.lastIndexOf(str, fromIndex);
    }


    public String substring(int beginIndex) {
        return value.substring(beginIndex);
    }


    public String substring(int beginIndex, int endIndex) {
        return value.substring(beginIndex, endIndex);
    }


    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return value.subSequence(beginIndex, endIndex);
    }


    public String concat(String str) {
        return value.concat(str);
    }


    public String replace(char oldChar, char newChar) {
        return value.replace(oldChar, newChar);
    }

    public boolean matches(String regex) {
        return value.matches(regex);
    }

    public boolean contains(CharSequence s) {
        return value.contains(s);
    }


    public String replaceFirst(String regex, String replacement) {
        return value.replaceFirst(regex, replacement);
    }


    public String replaceAll(String regex, String replacement) {
        return value.replaceAll(regex, replacement);
    }


    public String replace(CharSequence target, CharSequence replacement) {
        return value.replace(target, replacement);
    }


    public String[] split(String regex, int limit) {
        return value.split(regex, limit);
    }


    public String[] split(String regex) {
        return value.split(regex);
    }


    public static String join(CharSequence delimiter, CharSequence... elements) {
        return String.join(delimiter, elements);
    }


    public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
        return String.join(delimiter, elements);
    }


    public String toLowerCase(Locale locale) {
        return value.toLowerCase(locale);
    }


    public String toLowerCase() {
        return value.toLowerCase();
    }


    public String toUpperCase(Locale locale) {
        return value.toUpperCase(locale);
    }


    public String toUpperCase() {
        return value.toUpperCase();
    }


    public String trim() {
        return value.trim();
    }


    @Override
    public String toString() {
        return value.toString();
    }

    public char[] toCharArray() {
        return value.toCharArray();
    }


    public static String format(String format, Object... args) {
        return String.format(format, args);
    }


    public static String format(Locale l, String format, Object... args) {
        return String.format(l, format, args);
    }


    public static String valueOf(Object obj) {
        return String.valueOf(obj);
    }


    public static String valueOf(char[] data) {
        return String.valueOf(data);
    }


    public static String valueOf(char[] data, int offset, int count) {
        return String.valueOf(data, offset, count);
    }


    public static String copyValueOf(char[] data, int offset, int count) {
        return String.copyValueOf(data, offset, count);
    }


    public static String copyValueOf(char[] data) {
        return String.copyValueOf(data);
    }


    public static String valueOf(boolean b) {
        return String.valueOf(b);
    }


    public static String valueOf(char c) {
        return String.valueOf(c);
    }


    public static String valueOf(int i) {
        return String.valueOf(i);
    }


    public static String valueOf(long l) {
        return String.valueOf(l);
    }


    public static String valueOf(float f) {
        return String.valueOf(f);
    }


    public static String valueOf(double d) {
        return String.valueOf(d);
    }


    public String intern() {
        return value.intern();
    }

    @Override
    public IntStream chars() {
        return value.chars();
    }

    @Override
    public IntStream codePoints() {
        return value.codePoints();
    }

    private String value = null;

    @Override
    public Editable replace(int st, int en, CharSequence source, int start, int end) {
        return null;
    }

    @Override
    public Editable replace(int st, int en, CharSequence text) {
        return null;
    }

    @Override
    public Editable insert(int where, CharSequence text, int start, int end) {
        return null;
    }

    @Override
    public Editable insert(int where, CharSequence text) {
        return null;
    }

    @Override
    public Editable delete(int st, int en) {
        return null;
    }

    @Override
    public Editable append(CharSequence text) {
        return null;
    }

    @Override
    public Editable append(CharSequence text, int start, int end) {
        return null;
    }

    @Override
    public Editable append(char text) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void clearSpans() {

    }

    @Override
    public void setFilters(InputFilter[] filters) {

    }

    @Override
    public InputFilter[] getFilters() {
        return new InputFilter[0];
    }

    @Override
    public void setSpan(Object what, int start, int end, int flags) {

    }

    @Override
    public void removeSpan(Object what) {

    }

    @Override
    public <T> T[] getSpans(int start, int end, Class<T> type) {
        return null;
    }

    @Override
    public int getSpanStart(Object tag) {
        return 0;
    }

    @Override
    public int getSpanEnd(Object tag) {
        return 0;
    }

    @Override
    public int getSpanFlags(Object tag) {
        return 0;
    }

    @Override
    public int nextSpanTransition(int start, int limit, Class type) {
        return 0;
    }
}
