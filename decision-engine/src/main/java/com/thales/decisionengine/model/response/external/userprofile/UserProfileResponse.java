package com.thales.decisionengine.model.response.external.userprofile;

import com.thales.decisionengine.enums.Segment;

public record UserProfileResponse(Segment segment, int creditModifier, boolean hasDebt) {}
