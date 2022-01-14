## Goal is to override locale and theme for some specific fragments when using single activity architecture.

Our app was using Single Activity Architrecute using AndroidX navigation. Now we need to add multi-language support but in phase means some fragments will follow the locale of the Activity some Fragments need to show only in English and some need to be strict to any different locale.

To archive this we will create our own `ContextWrapper` and override the `getContext()` of the fragment

```kotlin
package dev.arpan.localized.fragment

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Resources
import android.view.LayoutInflater
import java.util.*

class ContextLocaleWrapper(base: Context, private val locale: Locale) : ContextWrapper(base) {
    private var _inflater: LayoutInflater? = null
    private var _resources: Resources? = null
    private var _theme: Resources.Theme? = null

    override fun getResources(): Resources {
        return _resources ?: run {
            val baseRes = super.getResources()
            val resContext = createConfigurationContext(
                baseRes.createLocalizedConfiguration(locale)
            )
            return@run resContext.resources
        }.also {
            _resources = it
        }
    }

    override fun getSystemService(name: String): Any {
        if (LAYOUT_INFLATER_SERVICE == name) {
            return _inflater ?: run {
                LayoutInflater.from(baseContext).cloneInContext(this)
            }.also {
                _inflater = it
            }
        }
        return super.getSystemService(name)
    }

    override fun getTheme(): Resources.Theme {
        return _theme ?: resources.newTheme().apply {
            setTo(super.getTheme())
        }.also {
            _theme = it
        }
    }

    override fun getAssets(): AssetManager {
        return resources.assets
    }
}
```
In this `ContextWrapper` we are overriding the `getResources()` to use resources fron new configuration Context, will have also override the `getAssets()` so it uses our correct configuration.
**NOTE:** we also need to override the `getTheme()` in `getTheme()` we are creating a new theme from our custom configuration resources and set it to the theme of the base context. If we dont this then if your base context (here activity) is using any different local the our then when the fragment is inflated then it will use the locale from the base context. (here activity)

we have also override the `getSystemService()` any call to `LayoutInflater.from(context)` will be cloned with the context of our custom `ContextWrapper`

now we can override the `getContext()` of the `BaseFragment` to wrap the context with our context wrapper
``` kotlin
private var wrappedCtx: Context? = null
...
...
// return the custom local from the fragment
protected open fun applyOverrideLocal(): Locale? {
    return null 
}
...
...
override fun getContext(): Context {
        return wrappedCtx ?: run {
            val baseCtx = checkNotNull(super.getContext()) {
                "Fragment $this not attached to a context."
            }
            val locale = applyOverrideLocal()
            when {
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
...
...
```
also need to override `onGetLayoutInflater()` so it uses inflater from out wrapped context. 

``` kotlin
...
...
override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        return super.onGetLayoutInflater(savedInstanceState).cloneInContext(requireContext())
}
...
...
```
now in `onCreateView()` we also need to fix the layout direction of the inflated view because if the host activity of fragment is using RTL layout direction then our inflated fragment will also use RTL layout direction. Now if the locale of the fragment is set to any LTR language then we need to fix the direction of the inflated fragment.
```kotlin
...
...
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
...
...
```


<img src="./readme/demo.gif" alt="Food App Menu" />
