package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.domain.model.WithdrawStatus
import com.paylivre.sdk.gateway.android.domain.model.WithdrawStatusOrder
import org.junit.Assert
import org.junit.Test

class StatusWithdrawUtilsTest {

    @Test
    fun `test cases getDataStatusWithdrawById`() {
        //NEW
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_awaiting_payment,
            R.string.status_awaiting_payment,
            R.drawable.ic_rotate_cw
        ), getDataStatusWithdrawById(WithdrawStatus.NEW.code))

        //APPROVED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_completed,
            R.string.msg_status_completed,
            R.drawable.ic_completed
        ), getDataStatusWithdrawById(WithdrawStatus.APPROVED.code))

        //CANCELLED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_cancelled,
            R.string.msg_status_cancelled,
            R.drawable.ic_cancelled
        ), getDataStatusWithdrawById(WithdrawStatus.CANCELLED.code))

        //REFUNDED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_refunded,
            R.string.status_refunded,
            R.drawable.ic_returned_complete
        ), getDataStatusWithdrawById(WithdrawStatus.REFUNDED.code))

        //ERROR
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_error,
            R.string.status_error,
            R.drawable.ic_error
        ), getDataStatusWithdrawById(WithdrawStatus.ERROR.code))

        //DEFAULT
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_incomplete,
            R.string.status_incomplete,
            R.drawable.ic_incomplete
        ), getDataStatusWithdrawById(-1))
    }

    @Test
    fun `test cases getDataStatusWithdrawOrderById`() {
        //NEW
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_awaiting_payment,
            R.string.status_awaiting_payment,
            R.drawable.ic_rotate_cw
        ), getDataStatusWithdrawOrderById(WithdrawStatusOrder.NEW.code))

        //PENDING
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_pending,
            R.string.msg_status_pending,
            R.drawable.ic_pending
        ), getDataStatusWithdrawOrderById(WithdrawStatusOrder.PENDING.code))

        //APPROVED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_completed,
            R.string.msg_status_completed,
            R.drawable.ic_completed
        ), getDataStatusWithdrawOrderById(WithdrawStatusOrder.APPROVED.code))

        //CANCELLED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_cancelled,
            R.string.msg_status_cancelled,
            R.drawable.ic_cancelled
        ), getDataStatusWithdrawOrderById(WithdrawStatusOrder.CANCELLED.code))

        //EXPIRED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_expired,
            R.string.msg_status_expired,
            R.drawable.ic_expired
        ), getDataStatusWithdrawOrderById(WithdrawStatusOrder.EXPIRED.code))

        //INCOMPLETE
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_incomplete,
            R.string.status_incomplete,
            R.drawable.ic_incomplete
        ), getDataStatusWithdrawOrderById(WithdrawStatusOrder.INCOMPLETE.code))

        //DEFAULT
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_incomplete,
            R.string.status_incomplete,
            R.drawable.ic_incomplete
        ), getDataStatusWithdrawOrderById(-1))
    }

    @Test
    fun `check method fromInt from MerchantApprovalStatusOrder`() {
        Assert.assertEquals(MerchantApprovalStatusOrder.NEW,
            MerchantApprovalStatusOrder.fromInt(0))

        Assert.assertEquals(MerchantApprovalStatusOrder.PENDING,
            MerchantApprovalStatusOrder.fromInt(1))

        Assert.assertEquals(MerchantApprovalStatusOrder.APPROVED,
            MerchantApprovalStatusOrder.fromInt(2))

        Assert.assertEquals(MerchantApprovalStatusOrder.CANCELLED,
            MerchantApprovalStatusOrder.fromInt(3))

        Assert.assertEquals(MerchantApprovalStatusOrder.EXPIRED,
            MerchantApprovalStatusOrder.fromInt(4))

        Assert.assertEquals(MerchantApprovalStatusOrder.INCOMPLETE,
            MerchantApprovalStatusOrder.fromInt(5))
    }

    @Test
    fun `test cases getDataStatusMerchantApprovalById`() {
        //NEW
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_new,
            R.string.status_new,
            R.drawable.ic_new
        ), getDataStatusMerchantApprovalById(WithdrawStatusOrder.NEW.code))

        //PENDING
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_pending,
            R.string.msg_status_pending,
            R.drawable.ic_pending
        ), getDataStatusMerchantApprovalById(WithdrawStatusOrder.PENDING.code))

        //APPROVED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_completed,
            R.string.msg_status_completed,
            R.drawable.ic_completed
        ), getDataStatusMerchantApprovalById(WithdrawStatusOrder.APPROVED.code))

        //CANCELLED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_cancelled,
            R.string.msg_status_cancelled,
            R.drawable.ic_cancelled
        ), getDataStatusMerchantApprovalById(WithdrawStatusOrder.CANCELLED.code))

        //EXPIRED
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_expired,
            R.string.msg_status_expired,
            R.drawable.ic_expired
        ), getDataStatusMerchantApprovalById(WithdrawStatusOrder.EXPIRED.code))

        //INCOMPLETE
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_incomplete,
            R.string.status_incomplete,
            R.drawable.ic_incomplete
        ), getDataStatusMerchantApprovalById(WithdrawStatusOrder.INCOMPLETE.code))

        //DEFAULT
        Assert.assertEquals(DataStatusWithdrawResponse(
            R.string.status_incomplete,
            R.string.status_incomplete,
            R.drawable.ic_incomplete
        ), getDataStatusMerchantApprovalById(-1))
    }
}