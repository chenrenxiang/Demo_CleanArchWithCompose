package com.clean.architecture.demo.common

import android.util.Log
import com.clean.architecture.demo.BuildConfig



/**
 * Now you can replace method:
 *  fun(a: Int, b: (value: Int) -> Unit)
 * with:
 *  fun(a: Int, b: OnResult<Int>)
 */
typealias OnResult<T> = (T) -> Unit

typealias OnClick = () -> Unit



fun log(tag: String = "Demo", msg: String) {
    if(BuildConfig.DEBUG) {
        Log.i(tag, msg)
    }
}
