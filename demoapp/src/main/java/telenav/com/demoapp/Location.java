package telenav.com.demoapp;

/**
 * Created by dima on 17/02/16.
 */
public class Location {
    private String name;
    private String url;
    private String population;
    private String density;
    private String area;
    private String description;
    private String timezone;
    private int iconResourceId;

    public Location(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public Location setTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public Location setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Location setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Location setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPopulation() {
        return population;
    }

    public Location setPopulation(String population) {
        this.population = population;
        return this;
    }

    public String getDensity() {
        return density;
    }

    public Location setDensity(String density) {
        this.density = density;
        return this;
    }

    public String getArea() {
        return area;
    }

    public Location setArea(String area) {
        this.area = area;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Location setDescription(String description) {
        this.description = description;
        return this;
    }
}
