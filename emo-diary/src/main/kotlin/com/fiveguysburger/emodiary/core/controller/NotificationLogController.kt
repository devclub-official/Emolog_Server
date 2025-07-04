@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.fiveguysburger.emodiary.core.controller

import com.fiveguysburger.emodiary.core.dto.NotificationLogResponseDto
import com.fiveguysburger.emodiary.core.entity.NotificationLog
import com.fiveguysburger.emodiary.core.enums.NotificationStatus
import com.fiveguysburger.emodiary.core.service.NotificationLogService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notification-logs")
@Tag(name = "알림 로그", description = "알림 로그 관리 API")
class NotificationLogController(
    private val notificationLogService: NotificationLogService,
) {
    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 알림 로그 조회", description = "특정 상태의 알림 로그를 조회합니다.")
    fun findByNotificationStatus(
        @PathVariable status: NotificationStatus,
    ): ResponseEntity<List<NotificationLog>> = ResponseEntity.ok(notificationLogService.findByNotificationStatus(status))

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 알림 이력 조회", description = "특정 사용자의 알림 이력을 조회합니다.")
    fun findUserNotificationHistory(
        @PathVariable userId: Int,
    ): ResponseEntity<List<NotificationLog>> = ResponseEntity.ok(notificationLogService.findUserNotificationHistory(userId))

    /**
     * 특정 사용자의 알림 이력을 조회합니다.
     * @param userId 사용자 ID
     * @return 알림 로그 목록
     */
    @GetMapping("/users/{userId}")
    fun getUserNotificationHistory(
        @PathVariable userId: Int,
    ): ResponseEntity<List<NotificationLogResponseDto>> {
        val logs = notificationLogService.findUserNotificationHistory(userId)
        return ResponseEntity.ok(logs.map { NotificationLogResponseDto.from(it) })
    }

    /**
     * 특정 일수 이상 지난 알림 로그를 삭제합니다.
     * @param days 삭제할 기준 일수
     * @return 삭제된 알림 로그 수
     */
    @DeleteMapping("/old")
    fun deleteOldNotificationLogs(
        @RequestParam(defaultValue = "30") days: Int,
    ): ResponseEntity<Map<String, Int>> {
        val deletedCount = notificationLogService.deleteOldNotificationLogs(days)
        return ResponseEntity.ok(mapOf("deletedCount" to deletedCount))
    }

    /**
     * 특정 알림 로그를 삭제합니다.
     * @param id 삭제할 알림 로그 ID
     */
    @DeleteMapping("/{id}")
    fun deleteNotificationLog(
        @PathVariable id: String,
    ): ResponseEntity<Unit> {
        notificationLogService.deleteNotificationLog(id)
        return ResponseEntity.noContent().build()
    }
}
