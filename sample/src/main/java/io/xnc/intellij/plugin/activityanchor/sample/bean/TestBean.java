package io.xnc.intellij.plugin.activityanchor.sample.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TestBean implements Parcelable {
    String name;
    int age;
    String desc;

    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", desc='" + desc + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeString(this.desc);
    }

    public TestBean() {
    }

    protected TestBean(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        this.desc = in.readString();
    }

    public static final Parcelable.Creator<TestBean> CREATOR = new Parcelable.Creator<TestBean>() {
        @Override
        public TestBean createFromParcel(Parcel source) {
            return new TestBean(source);
        }

        @Override
        public TestBean[] newArray(int size) {
            return new TestBean[size];
        }
    };
}
