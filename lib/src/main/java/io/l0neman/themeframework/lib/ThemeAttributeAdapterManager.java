package io.l0neman.themeframework.lib;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.collection.ArrayMap;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import io.l0neman.themeframework.lib.adapter.CardViewAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.CompoundButtonAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.ImageViewAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.ListViewAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.ProgressBarAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.SeekBarAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.SpinnerAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.SwitchAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.SwitchCompatAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.TextViewAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.ToolbarAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.ViewAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.design.FloatingActionButtonAttributeAdapter;
import io.l0neman.themeframework.lib.adapter.design.TabLayoutAttributeAdapter;

/**
 * 主题属性管理器，管理 View 和主题属性适配器的类型工厂模板
 */
public class ThemeAttributeAdapterManager {
  private static final Map<Class<? extends View>, ThemeAttributeAdapterFactory<? extends View>>
      VIEW_THEME_ATTRIBUTE_MAP = new ArrayMap<>();

  // 添加内置 View 类型主题属性适配支持
  static {
    // View
    VIEW_THEME_ATTRIBUTE_MAP.put(View.class, new DefaultThemeAttributeAdapterFactory<>(ViewAttributeAdapter.Impl.class));

    // TextView
    ThemeAttributeAdapterFactory<TextView> textViewThemeAttributeFactory =
        new DefaultThemeAttributeAdapterFactory<>(TextViewAttributeAdapter.Impl.class);
    VIEW_THEME_ATTRIBUTE_MAP.put(TextView.class, textViewThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatTextView.class, textViewThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatCheckedTextView.class, textViewThemeAttributeFactory);

    // ImageView
    ThemeAttributeAdapterFactory<ImageView> imageViewThemeAttributeFactory =
        new DefaultThemeAttributeAdapterFactory<>(ImageViewAttributeAdapter.Impl.class);
    VIEW_THEME_ATTRIBUTE_MAP.put(ImageView.class, imageViewThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatImageView.class, imageViewThemeAttributeFactory);

    // ImageButton
    VIEW_THEME_ATTRIBUTE_MAP.put(ImageButton.class, imageViewThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatImageButton.class, imageViewThemeAttributeFactory);

    // CardView
    VIEW_THEME_ATTRIBUTE_MAP.put(CardView.class, new DefaultThemeAttributeAdapterFactory<>(CardViewAttributeAdapter.Impl.class));

    // EditText
    VIEW_THEME_ATTRIBUTE_MAP.put(EditText.class, textViewThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatEditText.class, textViewThemeAttributeFactory);

    // Button
    VIEW_THEME_ATTRIBUTE_MAP.put(Button.class, textViewThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatButton.class, textViewThemeAttributeFactory);

    ThemeAttributeAdapterFactory<CompoundButton> compoundButtonThemeAttributeFactory =
        new DefaultThemeAttributeAdapterFactory<>(CompoundButtonAttributeAdapter.Impl.class);

    // ToggleButton
    VIEW_THEME_ATTRIBUTE_MAP.put(ToggleButton.class, compoundButtonThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatToggleButton.class, compoundButtonThemeAttributeFactory);

    // RadioButton
    VIEW_THEME_ATTRIBUTE_MAP.put(RadioButton.class, compoundButtonThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatRadioButton.class, compoundButtonThemeAttributeFactory);

    // CheckBox
    VIEW_THEME_ATTRIBUTE_MAP.put(CheckBox.class, compoundButtonThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatCheckBox.class, compoundButtonThemeAttributeFactory);

    // Switch
    VIEW_THEME_ATTRIBUTE_MAP.put(Switch.class, new DefaultThemeAttributeAdapterFactory<>(
        SwitchAttributeAdapter.Impl.class
    ));
    // SwitchCompat
    VIEW_THEME_ATTRIBUTE_MAP.put(SwitchCompat.class, new DefaultThemeAttributeAdapterFactory<>(
        SwitchCompatAttributeAdapter.Impl.class
    ));

    // Toolbar
    VIEW_THEME_ATTRIBUTE_MAP.put(Toolbar.class,
        new DefaultThemeAttributeAdapterFactory<>(ToolbarAttributeAdapter.Impl.class));

    // ProgressBar
    VIEW_THEME_ATTRIBUTE_MAP.put(ProgressBar.class, new DefaultThemeAttributeAdapterFactory<>(
        ProgressBarAttributeAdapter.Impl.class
    ));

    // Spinner
    VIEW_THEME_ATTRIBUTE_MAP.put(Spinner.class, new DefaultThemeAttributeAdapterFactory<>(
        SpinnerAttributeAdapter.Impl.class
    ));
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatSpinner.class, new DefaultThemeAttributeAdapterFactory<>(
        SpinnerAttributeAdapter.Impl.class
    ));

    // SeekBar
    DefaultThemeAttributeAdapterFactory<SeekBar> seekBarThemeAttributeFactory =
        new DefaultThemeAttributeAdapterFactory<>(SeekBarAttributeAdapter.Impl.class);
    VIEW_THEME_ATTRIBUTE_MAP.put(SeekBar.class, seekBarThemeAttributeFactory);
    VIEW_THEME_ATTRIBUTE_MAP.put(AppCompatSeekBar.class, seekBarThemeAttributeFactory);

    // ListView
    VIEW_THEME_ATTRIBUTE_MAP.put(ListView.class, new DefaultThemeAttributeAdapterFactory<>(
        ListViewAttributeAdapter.Impl.class
    ));

    // design lib widgets BEGIN >>>>>>>>>>>>>>>>>>

    // TabLayout
    VIEW_THEME_ATTRIBUTE_MAP.put(TabLayout.class,
        new DefaultThemeAttributeAdapterFactory<>(TabLayoutAttributeAdapter.Impl.class));

    // FloatingActionButton
    VIEW_THEME_ATTRIBUTE_MAP.put(FloatingActionButton.class,
        new DefaultThemeAttributeAdapterFactory<>(FloatingActionButtonAttributeAdapter.Impl.class));

    // design lib widgets END >>>>>>>>>>>>>>>>>>>>>
  }

  private static final DefaultThemeAttributeAdapterFactory<View> DEFAULT_THEME_ATTRIBUTE_FACTORY =
      new DefaultThemeAttributeAdapterFactory<>(ViewAttributeAdapter.Impl.class);

  /**
   * 提供额外的 View 和主题属性适配器的映射，用于自定义 View 的支持以及扩展内置的类型
   *
   * @param viewType                     View 类型
   * @param themeAttributeAdapterFactory 主题属性适配器对象生成工厂，如果是自定义的主题属性类具有默认构无参造器
   *                                     ，可使用 {@link DefaultThemeAttributeAdapterFactory}
   * @param <T>                          View 泛型类型
   */
  public static <T extends View> void registerThemeAttribute(
      Class<T> viewType, ThemeAttributeAdapterFactory<? super T> themeAttributeAdapterFactory) {
    VIEW_THEME_ATTRIBUTE_MAP.put(viewType, themeAttributeAdapterFactory);
  }

  /**
   * 取消 View 和主题属性的映射，如有需要
   *
   * @param viewType View 类型
   */
  public static void unregisterThemeAttribute(Class<? extends View> viewType) {
    VIEW_THEME_ATTRIBUTE_MAP.remove(viewType);
  }

  /**
   * 根据 View 类型获取对应的主题属性对象工厂，使用默认构造器创建对象的工厂为 {@link #DEFAULT_THEME_ATTRIBUTE_FACTORY}
   *
   * @param viewType View 类型 class
   *
   * @return 主题属性适配器对象生成工厂对象
   */
  public static <T extends View> ThemeAttributeAdapterFactory<? super T> getFactory(Class<T> viewType) {
    // noinspection unchecked
    ThemeAttributeAdapterFactory<? super T> attributeFactory = (ThemeAttributeAdapterFactory<? super T>) VIEW_THEME_ATTRIBUTE_MAP.get(viewType);
    if (attributeFactory == null)
      // 没有注册 View 类型，则可使用默认 View 属性
      attributeFactory = DEFAULT_THEME_ATTRIBUTE_FACTORY;

    return attributeFactory;
  }

  /** 根据 View 类型创建对应的处理这个 View 类型的主题属性适配器对象 */
  public interface ThemeAttributeAdapterFactory<T extends View> {
    @NonNull ThemeAttributeAdapter<T> create();
  }

  /** 默认实现，直接通过默认无参构造器生成对象 */
  public static class DefaultThemeAttributeAdapterFactory<T extends View> implements ThemeAttributeAdapterFactory<T> {
    private final Class<? extends ThemeAttributeAdapter<? extends T>> themeAttributeClass;

    public DefaultThemeAttributeAdapterFactory(Class<? extends ThemeAttributeAdapter<? extends T>> themeAttributeClass) {
      this.themeAttributeClass = themeAttributeClass;
    }

    @NonNull @Override public ThemeAttributeAdapter<T> create() {
      try {
        // noinspection unchecked
        return (ThemeAttributeAdapter<T>) themeAttributeClass.newInstance();
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
  }
}
