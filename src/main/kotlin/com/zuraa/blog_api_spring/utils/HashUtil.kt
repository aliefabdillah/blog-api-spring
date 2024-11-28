package com.zuraa.blog_api_spring.utils

import org.springframework.security.crypto.bcrypt.BCrypt

class HashUtil {
    /*
    * Create hashed password
    * @return hashed password as string
    * */
    fun hashBcrypt(input: String): String {
        return BCrypt.hashpw(input, BCrypt.gensalt(10))
    }

    /*
    * Check input string and hashed password
    * @return true or false
    * */
    fun checkBcrypt(input: String, hash: String): Boolean {
        return BCrypt.checkpw(input, hash)
    }
}