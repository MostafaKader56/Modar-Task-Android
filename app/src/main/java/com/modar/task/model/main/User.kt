package com.modar.task.model.main

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.modar.task.R
import com.modar.task.enums.EditTextType

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val jobTitle: String,
    val gender: Gender
) {
    class CreateUserRequest(
        val name: String? = null,
        val age: Int? = null,
        val jobTitle: String? = null,
        val gender: Gender? = null,
    ) {
        fun validate(listener: Listener) {
            var isValid = true

            if (name.isNullOrEmpty()) {
                listener.error(EditTextType.NAME, R.string.error_name_empty)
                isValid = false
            }

            if (age == null) {
                listener.error(EditTextType.AGE, R.string.error_age_empty)
                isValid = false
            }

            if (jobTitle.isNullOrEmpty()) {
                listener.error(EditTextType.JOB_TITLE, R.string.error_job_title_empty)
                isValid = false
            }

            if (gender == null) {
                listener.error(EditTextType.GENDER, R.string.error_gender_empty)
                isValid = false
            }

            if (isValid) {
                listener.success(
                    User(
                        name = name!!,
                        age = age!!,
                        jobTitle = jobTitle!!,
                        gender = gender!!,
                    ),
                )
            }
        }

        interface Listener {
            fun success(user: User)
            fun error(type: EditTextType, errorRes: Int?)
        }
    }
}