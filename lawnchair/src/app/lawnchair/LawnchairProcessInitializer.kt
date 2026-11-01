package app.lawnchair

import android.content.Context
import androidx.annotation.Keep
import app.lawnchair.bugreport.LawnchairBugReporter
import com.android.quickstep.QuickstepProcessInitializer
import javax.inject.Inject

@Keep
class LawnchairProcessInitializer @Inject constructor() : QuickstepProcessInitializer() {

    override fun init(context: Context) {
        LawnchairBugReporter.INSTANCE.get(context)
        super.init(context)
    }
}
