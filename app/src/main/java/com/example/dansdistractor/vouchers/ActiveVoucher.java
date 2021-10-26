package com.example.dansdistractor.vouchers;

/**
 * @ClassName: Activate
 * @Description:
 * @Author: wongchihaul
 * @CreateDate: 2021/10/15 11:44 下午
 */
public class ActiveVoucher extends VoucherTab {
    public ActiveVoucher() {
        super.setStatus(VoucherRecycleAdaptor.ACTIVE);
    }
}
