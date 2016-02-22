package telenav.com.demoapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Arrays;

import telenav.com.nodeflow.Node;
import telenav.com.nodeflow.NodeFlow;
import telenav.com.nodeflow.OnActiveNodeChangeListener;

/**
 * Created by dima on 17/02/16.
 */
public class LocationFlow extends NodeFlow {
    public LocationFlow(Context context) {
        super(context);
    }

    public LocationFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocationFlow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialize() {
        final int duration = 500;
        setNodeChangeListener(new OnActiveNodeChangeListener() {
            @Override
            public void onNodeOpened(View view, Node<?> node) {//happens after node opening translation animation is finished and views are reset
                if (!node.hasChildren())
                    view.findViewById(R.id.list_item_favicon).animate().alpha(1).setDuration(duration).setInterpolator(new FastOutSlowInInterpolator());
                ((ImageView) view.findViewById(R.id.list_item_image)).setImageResource(R.drawable.ic_close_white_36dp);
            }

            @Override
            public void onNodeClosing(View view, Node<?> node) {//happens before node closing translation animation
                if (!node.hasChildren())
                    view.findViewById(R.id.list_item_favicon).animate().alpha(0).setDuration(duration / 2).setInterpolator(new FastOutSlowInInterpolator());
                ((ImageView) view.findViewById(R.id.list_item_image)).setImageResource(((Location) node.getData()).getIconResourceId());
            }

            @Override
            public void onParentNodeOpening(View view, Node<?> node) {//happens before node closing translation animation
                ((ImageView) view.findViewById(R.id.list_item_image)).setImageResource(R.drawable.ic_close_white_36dp);
            }

            @Override
            public void onParentNodeOpened(View view, Node<?> node) {//happens after node closing translation animation is finished and views are reset
                ((ImageView) view.findViewById(R.id.list_item_image)).setImageResource(R.drawable.ic_close_white_36dp);
            }
        });
        setAnimationDuration(duration);
        super.initialize();
    }

    @Override
    protected Node<?> getRootNode() {
        return Node.get(new Location("root"))
                .addChildNodes(Arrays.asList(
                        Node.get(new Location("Asia").setIconResourceId(R.drawable.ic_directions_transit_white_36dp)),
                        Node.get(new Location("Europe").setIconResourceId(R.drawable.ic_directions_car_white_36dp))
                                .addChildNodes(Arrays.asList(
                                        Node.get(new Location("Austria").setIconResourceId(R.drawable.ic_directions_car_white_36dp))
                                                .addChildren(Arrays.asList(
                                                        new Location("Linz").setIconResourceId(R.drawable.ic_directions_car_white_36dp),
                                                        new Location("Vienna").setIconResourceId(R.drawable.ic_directions_car_white_36dp).setArea("414.65 km2").setPopulation("1,840,573").setDensity("4,326.1/km2").setTimezone("CET (UTC+1)").setUrl("https://upload.wikimedia.org/wikipedia/commons/e/e9/13-08-30-wien-by-RalfR-123.jpg")
                                                                .setDescription("Vienna is the capital and largest city of Austria, and one of the nine states of Austria. Vienna is Austria's primary city, with a population of about 1.8 million (2.6 million within the metropolitan area, nearly one third of Austria's population), and its cultural, economic, and political centre. It is the 7th-largest city by population within city limits in the European Union. Until the beginning of the 20th century it was the largest German-speaking city in the world, and before the splitting of the Austro-Hungarian Empire in World War I the city had 2 million inhabitants. Today it has the second largest number of German speakers after Berlin. Vienna is host to many major international organizations, including the United Nations and OPEC. The city lies in the east of Austria and is close to the borders of the Czech Republic, Slovakia, and Hungary. These regions work together in a European Centrope border region.")
                                                )),
                                        Node.get(new Location("Belgium").setIconResourceId(R.drawable.ic_directions_car_white_36dp))
                                                .addChildren(Arrays.asList(
                                                        new Location("Bruxelles").setIconResourceId(R.drawable.ic_directions_car_white_36dp),
                                                        new Location("Bruges").setIconResourceId(R.drawable.ic_directions_car_white_36dp)
                                                )))),
                        Node.get(new Location("North America").setIconResourceId(R.drawable.ic_directions_boat_white_36dp))
                                .addChildNodes(Arrays.asList(
                                        Node.get(new Location("United States").setIconResourceId(R.drawable.ic_directions_boat_white_36dp))
                                                .addChildren(Arrays.asList(
                                                        new Location("Alaska").setIconResourceId(R.drawable.ic_directions_boat_white_36dp),
                                                        new Location("California").setIconResourceId(R.drawable.ic_directions_boat_white_36dp).setArea("423,970 km2").setPopulation("39,144,818").setDensity("95.0/km2").setTimezone("PST (UTC−8)").setUrl("https://upload.wikimedia.org/wikipedia/commons/c/c2/Golden_Gate_Bridge%2C_SF_%28cropped%29.jpg").setDescription("California is a state located on the West Coast of the United States. It is the most populous U.S. state, with 39 million people, and the third largest state by area (after Alaska and Texas). California is bordered by Oregon to the north, Nevada to the east, Arizona to the southeast, and the Mexican state of Baja California to the south. It contains the nation's second most populous census statistical area (Greater Los Angeles Area) and the fifth most populous (San Francisco Bay Area), and eight of the nation's 50 most populated cities (Los Angeles, San Diego, San Jose, San Francisco, Fresno, Sacramento, Long Beach, and Oakland). Sacramento has been the state capital since 1854.\n" +
                                                                "\nWhat is now California was first settled by various Native American tribes before being explored by a number of European expeditions during the 16th and 17th centuries. It was then claimed by the Spanish Empire as part of Alta California in the larger territory of New Spain."),
                                                        new Location("Colorado").setIconResourceId(R.drawable.ic_directions_boat_white_36dp),
                                                        new Location("Ohio").setIconResourceId(R.drawable.ic_directions_boat_white_36dp),
                                                        new Location("Washington").setIconResourceId(R.drawable.ic_directions_boat_white_36dp)
                                                )),
                                        Node.get(new Location("Mexico").setIconResourceId(R.drawable.ic_directions_boat_white_36dp))
                                                .addChildren(Arrays.asList(
                                                        new Location("Tijuana").setIconResourceId(R.drawable.ic_directions_boat_white_36dp)
                                                ))))
                ));
    }

    @Override
    protected View getContentView(Node<?> node) {
        Location data = (Location) node.getData();
        ViewGroup v = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.content, this, false);
        ((TextView) v.findViewById(R.id.content_title)).setText(data.getName());
        ((TextView) v.findViewById(R.id.content_subtitle)).setText("Population " + data.getPopulation());
        ((TextView) v.findViewById(R.id.content_subtitle2)).setText("Density " + data.getDensity());
        ((TextView) v.findViewById(R.id.content_subtitle3)).setText("Area " + data.getArea());
        ((TextView) v.findViewById(R.id.content_subtitle4)).setText("Timezone " + data.getTimezone());
        ((TextView) v.findViewById(R.id.content_text)).setText(data.getDescription());
        if (data.getUrl() != null && !data.getUrl().isEmpty()) {
            Uri uri = Uri.parse(data.getUrl());
            ((SimpleDraweeView) v.findViewById(R.id.content_image)).setImageURI(uri);
        }
        return v;
    }

    @Override
    protected View getHeaderView(final Node<?> node) {
        Location data = (Location) node.getData();
        ViewGroup view = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.oneline_header, this, false);
        ((TextView) view.findViewById(R.id.list_item_text)).setText(data.getName());
        ((ImageView) view.findViewById(R.id.list_item_image)).setImageResource(data.getIconResourceId());
        switch (node.getDepth()) {
            case 1:
                view.getBackground().setColorFilter(Color.parseColor("#443266"), PorterDuff.Mode.SRC_IN);
                break;
            case 2:
                view.getBackground().setColorFilter(Color.parseColor("#665488"), PorterDuff.Mode.SRC_IN);
                break;
            case 3:
            default:
                view.getBackground().setColorFilter(Color.parseColor("#8876AA"), PorterDuff.Mode.SRC_IN);
                break;
        }
        return view;
    }
}
