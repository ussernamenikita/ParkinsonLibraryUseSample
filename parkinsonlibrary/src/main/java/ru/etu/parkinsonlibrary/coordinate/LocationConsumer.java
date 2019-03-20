package ru.etu.parkinsonlibrary.coordinate;
import android.location.Location;

public interface LocationConsumer {
    void onLocation(Location location);
}

