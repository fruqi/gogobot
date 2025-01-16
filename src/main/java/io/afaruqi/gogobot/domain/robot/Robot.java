package io.afaruqi.gogobot.domain.robot;

import io.afaruqi.gogobot.domain.common.Coordinate;

public record Robot(
    Coordinate coordinate,
    FaceDirection faceDirection
) {
}
