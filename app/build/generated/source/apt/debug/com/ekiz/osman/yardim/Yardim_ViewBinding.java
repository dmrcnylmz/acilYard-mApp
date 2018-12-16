// Generated code from Butter Knife. Do not modify!
package com.ekiz.osman.yardim;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Yardim_ViewBinding implements Unbinder {
  private Yardim target;

  private View view2131230789;

  @UiThread
  public Yardim_ViewBinding(Yardim target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Yardim_ViewBinding(final Yardim target, View source) {
    this.target = target;

    View view;
    target.txtLocationResult = Utils.findRequiredViewAsType(source, R.id.location_result, "field 'txtLocationResult'", TextView.class);
    target.txtUpdatedOn = Utils.findRequiredViewAsType(source, R.id.updated_on, "field 'txtUpdatedOn'", TextView.class);
    view = Utils.findRequiredView(source, R.id.durumgonder, "field 'durumgonder' and method 'stopLocationButtonClick'");
    target.durumgonder = Utils.castView(view, R.id.durumgonder, "field 'durumgonder'", Button.class);
    view2131230789 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.stopLocationButtonClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Yardim target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.txtLocationResult = null;
    target.txtUpdatedOn = null;
    target.durumgonder = null;

    view2131230789.setOnClickListener(null);
    view2131230789 = null;
  }
}
