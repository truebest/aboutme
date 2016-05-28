package shilin.aboutme.datas;

/**
 * Created by beerko on 09.05.16.
 */
public class Skills {
    private String  name;
    private int stars;
    private String description;

    public Skills() {

    }

    public Skills(String name, int stars, String description) {
        this.name = name;
        this.stars = stars;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {

        return name;
    }

    public int getStars() {
        return stars;
    }

    public String getDescription() {
        return description;
    }
}
