package com.geekymusketeers.medify.utils

import com.geekymusketeers.medify.utils.Validator.Companion.isValidEmail
import com.geekymusketeers.medify.utils.Validator.Companion.isValidPhone
import org.junit.Assert.*
import org.junit.Test


class ValidatorTest {

    /************ Email Unit Tests ****************/

    @Test
    fun `email is empty` () {
        val email = ""
        val result = email.isValidEmail()
        assertFalse(result)
    }

    @Test
    fun `email is wrong` () {
        val email = "abc"
        val result = email.isValidEmail()
        assertFalse(result)
    }

    @Test
    fun `email is correct` () {
        val email = "binayshaw7777@gmail.com"
        val result = email.isValidEmail()
        assertTrue(result)
    }

    @Test
    fun `email is correct with space` () {
        val email = "binay shaw 7777 @gmail . com"
        val result = email.isValidEmail()
        assertFalse(result)
    }

    @Test
    fun `email is without dot` () {
        val email = "binay shaw 7777 @gmail com"
        val result = email.isValidEmail()
        assertFalse(result)
    }

    @Test
    fun `email is without at the rate` () {
        val email = "binay shaw 7777 gmail . com"
        val result = email.isValidEmail()
        assertFalse(result)
    }

    @Test
    fun `email is not empty but dot com is missing` () {
        val email = "binayshaw7777@gmail"
        val result = email.isValidEmail()
        assertTrue(result)
    }

    @Test
    fun `email is not empty but prefix name is missing` () {
        val email = "@gmail.com"
        val result = email.isValidEmail()
        assertFalse(result)
    }

    /************ Phone Number Unit Tests ****************/

    @Test
    fun `phone number is empty` () {
        val phoneNumber = ""
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is wrong` () {
        val phoneNumber = "abc"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct` () {
        val phoneNumber = "1234567890"
        val result = phoneNumber.isValidPhone()
        assertTrue(result)
    }

    @Test
    fun `phone number is correct with space` () {
        val phoneNumber = "123 456 7890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct with dot` () {
        val phoneNumber = "123.456.7890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct with dash` () {
        val phoneNumber = "123-456-7890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct with brackets` () {
        val phoneNumber = "(123)4567890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct with brackets and dash` () {
        val phoneNumber = "(123)-456-7890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct with brackets and dot` () {
        val phoneNumber = "(123).456.7890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct with brackets and space` () {
        val phoneNumber = "(123) 456 7890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct with brackets and space and dot` () {
        val phoneNumber = "(123) 456.7890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is correct with brackets and space and dash` () {
        val phoneNumber = "(123) 456-7890"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is less than 10 digits` () {
        val phoneNumber = "1234567"
        val result = phoneNumber.isValidPhone()
        assertFalse(result)
    }

    @Test
    fun `phone number is more than 10 digits` () {
        val phoneNumber = "12345678901"
        val result = phoneNumber.isValidPhone()
        assertTrue(result)
    }
}