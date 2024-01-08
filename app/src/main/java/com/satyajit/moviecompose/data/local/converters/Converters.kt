package com.satyajit.moviecompose.data.local.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.satyajit.moviecompose.modals.Urls
import com.satyajit.moviecompose.modals.User

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun fromUrls(urls: Urls): String {
        return urls.regular
    }

    @TypeConverter
    fun toUrls(urls: String): Urls {
        return Urls(urls)
    }

    @TypeConverter
    fun fromUser(user: User): String {
        return user.username
    }

    @TypeConverter
    fun toUser(user: String): User {
        return User(user)
    }


}