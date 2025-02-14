package com.modar.task.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ViewModelScoped

// Base Class for ViewModels in the project where it's logic differ from project to another
@ViewModelScoped
abstract class BaseViewModel : ViewModel() {

}