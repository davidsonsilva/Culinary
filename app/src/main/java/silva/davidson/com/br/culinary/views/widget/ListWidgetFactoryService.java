package silva.davidson.com.br.culinary.views.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListWidgetFactoryService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext());
    }
}
