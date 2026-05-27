package com.kbase.project.dto;

import com.kbase.project.model.MemberRole;

public record ProjectMemberResponse(Long projectId, Long userId, MemberRole role, Boolean active) {
}
