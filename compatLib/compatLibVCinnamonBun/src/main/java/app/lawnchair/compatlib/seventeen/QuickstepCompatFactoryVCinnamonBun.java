package app.lawnchair.compatlib.seventeen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import app.lawnchair.compatlib.ActivityManagerCompat;
import app.lawnchair.compatlib.sixteen.QuickstepCompatFactoryVBaklava;

@RequiresApi(37)
public class QuickstepCompatFactoryVCinnamonBun extends QuickstepCompatFactoryVBaklava {

    @NonNull
    @Override
    public ActivityManagerCompat getActivityManagerCompat() {
        return new ActivityManagerCompatVCinnamonBun();
    }
}
