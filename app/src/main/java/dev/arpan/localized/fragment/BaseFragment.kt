/*
 * Copyright 2021 TravellerPass
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.arpan.localized.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
open class BaseFragment<out Binding : ViewBinding>(@LayoutRes private val contentLayoutId: Int) :
    Fragment() {

    private var _binding: Binding? = null
    val binding: Binding
        get() = _binding ?: throw IllegalStateException(
            "Fragment $this binding cannot be accessed before onCreateView() or " +
                    "after onDestroyView() is called."
        )

    private var wrappedCtx: Context? = null

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        return super.onGetLayoutInflater(savedInstanceState).cloneInContext(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(contentLayoutId, container, false)
        val locale = applyOverrideLocal()
        if (locale != null) {
            ViewCompat.setLayoutDirection(
                view,
                TextUtilsCompat.getLayoutDirectionFromLocale(locale)
            )
        }
        _binding = DataBindingUtil.bind(view)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        val themeContext = view?.context ?: return

        val systemBarColor = themeContext.getThemeColorAttribute(android.R.attr.windowBackground)
        setStatusBarColor(systemBarColor)
        setNavigationBarColor(systemBarColor)
    }

    protected fun setStatusBarColor(color: Int) {
        val window = activity?.window ?: return
        window.statusBarColor = color
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
            !color.isDarkColor()
    }

    protected fun setNavigationBarColor(color: Int) {
        val window = activity?.window ?: return
        window.navigationBarColor = color
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
            !color.isDarkColor()
    }

    override fun getContext(): Context {
        return wrappedCtx ?: run {
            val baseCtx = checkNotNull(super.getContext()) {
                "Fragment $this not attached to a context."
            }
            val locale = applyOverrideLocal()
            when {
                applyOverrideTheme() != 0 -> {
                    ContextThemeWrapper(baseCtx, applyOverrideTheme()).apply {
                        if (locale != null) {
                            applyOverrideConfiguration(
                                baseCtx.createLocalizedConfiguration(
                                    locale
                                )
                            )
                        }
                    }
                }
                locale != null -> {
                    ContextLocaleWrapper(baseCtx, locale)
                }
                else -> {
                    baseCtx
                }
            }.also {
                wrappedCtx = it
            }
        }
    }

    protected open fun applyOverrideTheme(): Int {
        return 0
    }

    protected open fun applyOverrideLocal(): Locale? {
        return null
    }
}
