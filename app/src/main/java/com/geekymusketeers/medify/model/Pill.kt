package com.geekymusketeers.medify.model

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.geekymusketeers.medify.R

data class Pill(
    @Embedded val pillEntity: PillEntity,
    @Relation(
        parentColumn = "pillId",
        entityColumn = "pillId"
    )
    var reminders: List<Reminder>
) : BaseModel() {

    @Ignore
    override var itemType: ItemType = ItemType.PILL

    companion object {

        fun new() = Pill(
            PillEntity(
                "",
                "",
                false,
                ReminderOptions.empty()
            ), listOf()
        )
    }

    var name
        get() = pillEntity.name
        set(value) {
            pillEntity.name = value
        }

    var description
        get() = pillEntity.description
        set(value) {
            pillEntity.description = value
        }

    fun getNotificationDescription(context: Context, reminder: Reminder) =
        context.getString(
            R.string.it_is_time_to_take,
            reminder.amount
        )

    var deleted
        get() = pillEntity.deleted
        set(value) {
            pillEntity.deleted = value
        }

    val id
        get() = pillEntity.id

    var options
        get() = pillEntity.options
        set(value) {
            pillEntity.options = value
        }

    var lastReminderDate
        get() = if (pillEntity.options.isIndefinite()) {
            null
        } else {
            pillEntity.options.lastReminderDate
        }
        set(value) {
            if (pillEntity.options.isIndefinite()) {
                pillEntity.options.lastReminderDate = null
            } else {
                pillEntity.options.lastReminderDate = value
            }
        }
    val isDescriptionVisible
        get() = pillEntity.description?.isNotBlank() ?: false




    override fun isSame(newItem: BaseModel) =
        if (newItem is Pill) {
            this.pillEntity.id == newItem.pillEntity.id
        } else false


    override fun isContentSame(newItem: BaseModel) =
        if (newItem is Pill) {
            this.name == newItem.name &&
                    this.description == newItem.description &&
                    this.deleted == newItem.deleted &&
                    this.options.isSame(newItem.options) &&
                    this.reminders.containsAll(newItem.reminders) &&
                    newItem.reminders.containsAll(this.reminders)
        } else false
}
