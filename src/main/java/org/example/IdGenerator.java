package org.example;

import java.util.List;
import java.util.Optional;

public class IdGenerator {

    public int getNewId(List<Distribution> distributions) {
        for (int i = 0; i < distributions.size(); i++) {
            int currentId = i + 1;

            Optional<Distribution> distribution = distributions.stream()
                    .filter(d -> d.getId() == currentId)
                    .findFirst();

            if (distribution.isEmpty()) {
                return currentId;
            }
        }

        return distributions.size() + 1;
    }

}
