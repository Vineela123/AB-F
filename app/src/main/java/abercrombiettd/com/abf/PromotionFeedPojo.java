package abercrombiettd.com.abf;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vineela on 5/12/2016.
 */

    class Buttons implements Serializable{
        String target;
        String title;

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
    class Promotions implements Serializable{
        Buttons buttons;
        String description;
        String footer;
        String image;
        String title1;
        Bitmap bmp;

        public Bitmap getBmp() {
            return bmp;
        }

        public void setBmp(Bitmap bmp) {
            this.bmp = bmp;
        }

        public Buttons getButtons() {
            return buttons;
        }

        public void setButtons(Buttons buttons) {
            this.buttons = buttons;
        }

        public String getTitle1() {
            return title1;
        }

        public void setTitle1(String title1) {
            this.title1 = title1;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFooter() {
            return footer;
        }

        public void setFooter(String footer) {
            this.footer = footer;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }



