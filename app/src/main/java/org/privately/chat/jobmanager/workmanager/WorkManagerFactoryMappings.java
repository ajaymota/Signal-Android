package org.privately.chat.jobmanager.workmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.privately.chat.jobs.AttachmentDownloadJob;
import org.privately.chat.jobs.AttachmentUploadJob;
import org.privately.chat.jobs.AvatarGroupsV1DownloadJob;
import org.privately.chat.jobs.CleanPreKeysJob;
import org.privately.chat.jobs.CreateSignedPreKeyJob;
import org.privately.chat.jobs.DirectoryRefreshJob;
import org.privately.chat.jobs.FailingJob;
import org.privately.chat.jobs.FcmRefreshJob;
import org.privately.chat.jobs.LocalBackupJob;
import org.privately.chat.jobs.MmsDownloadJob;
import org.privately.chat.jobs.MmsReceiveJob;
import org.privately.chat.jobs.MmsSendJob;
import org.privately.chat.jobs.MultiDeviceBlockedUpdateJob;
import org.privately.chat.jobs.MultiDeviceConfigurationUpdateJob;
import org.privately.chat.jobs.MultiDeviceContactUpdateJob;
import org.privately.chat.jobs.MultiDeviceGroupUpdateJob;
import org.privately.chat.jobs.MultiDeviceProfileKeyUpdateJob;
import org.privately.chat.jobs.MultiDeviceReadUpdateJob;
import org.privately.chat.jobs.MultiDeviceVerifiedUpdateJob;
import org.privately.chat.jobs.PushDecryptMessageJob;
import org.privately.chat.jobs.PushGroupSendJob;
import org.privately.chat.jobs.PushGroupUpdateJob;
import org.privately.chat.jobs.PushMediaSendJob;
import org.privately.chat.jobs.PushNotificationReceiveJob;
import org.privately.chat.jobs.PushTextSendJob;
import org.privately.chat.jobs.RefreshAttributesJob;
import org.privately.chat.jobs.RefreshPreKeysJob;
import org.privately.chat.jobs.RequestGroupInfoJob;
import org.privately.chat.jobs.RetrieveProfileAvatarJob;
import org.privately.chat.jobs.RetrieveProfileJob;
import org.privately.chat.jobs.RotateCertificateJob;
import org.privately.chat.jobs.RotateProfileKeyJob;
import org.privately.chat.jobs.RotateSignedPreKeyJob;
import org.privately.chat.jobs.SendDeliveryReceiptJob;
import org.privately.chat.jobs.SendReadReceiptJob;
import org.privately.chat.jobs.ServiceOutageDetectionJob;
import org.privately.chat.jobs.SmsReceiveJob;
import org.privately.chat.jobs.SmsSendJob;
import org.privately.chat.jobs.SmsSentJob;
import org.privately.chat.jobs.TrimThreadJob;
import org.privately.chat.jobs.TypingSendJob;
import org.privately.chat.jobs.UpdateApkJob;

import java.util.HashMap;
import java.util.Map;

public class WorkManagerFactoryMappings {

  private static final Map<String, String> FACTORY_MAP = new HashMap<String, String>() {{
    put("AttachmentDownloadJob", AttachmentDownloadJob.KEY);
    put("AttachmentUploadJob", AttachmentUploadJob.KEY);
    put("AvatarDownloadJob", AvatarGroupsV1DownloadJob.KEY);
    put("CleanPreKeysJob", CleanPreKeysJob.KEY);
    put("CreateSignedPreKeyJob", CreateSignedPreKeyJob.KEY);
    put("DirectoryRefreshJob", DirectoryRefreshJob.KEY);
    put("FcmRefreshJob", FcmRefreshJob.KEY);
    put("LocalBackupJob", LocalBackupJob.KEY);
    put("MmsDownloadJob", MmsDownloadJob.KEY);
    put("MmsReceiveJob", MmsReceiveJob.KEY);
    put("MmsSendJob", MmsSendJob.KEY);
    put("MultiDeviceBlockedUpdateJob", MultiDeviceBlockedUpdateJob.KEY);
    put("MultiDeviceConfigurationUpdateJob", MultiDeviceConfigurationUpdateJob.KEY);
    put("MultiDeviceContactUpdateJob", MultiDeviceContactUpdateJob.KEY);
    put("MultiDeviceGroupUpdateJob", MultiDeviceGroupUpdateJob.KEY);
    put("MultiDeviceProfileKeyUpdateJob", MultiDeviceProfileKeyUpdateJob.KEY);
    put("MultiDeviceReadUpdateJob", MultiDeviceReadUpdateJob.KEY);
    put("MultiDeviceVerifiedUpdateJob", MultiDeviceVerifiedUpdateJob.KEY);
    put("PushContentReceiveJob", FailingJob.KEY);
    put("PushDecryptJob", PushDecryptMessageJob.KEY);
    put("PushGroupSendJob", PushGroupSendJob.KEY);
    put("PushGroupUpdateJob", PushGroupUpdateJob.KEY);
    put("PushMediaSendJob", PushMediaSendJob.KEY);
    put("PushNotificationReceiveJob", PushNotificationReceiveJob.KEY);
    put("PushTextSendJob", PushTextSendJob.KEY);
    put("RefreshAttributesJob", RefreshAttributesJob.KEY);
    put("RefreshPreKeysJob", RefreshPreKeysJob.KEY);
    put("RefreshUnidentifiedDeliveryAbilityJob", FailingJob.KEY);
    put("RequestGroupInfoJob", RequestGroupInfoJob.KEY);
    put("RetrieveProfileAvatarJob", RetrieveProfileAvatarJob.KEY);
    put("RetrieveProfileJob", RetrieveProfileJob.KEY);
    put("RotateCertificateJob", RotateCertificateJob.KEY);
    put("RotateProfileKeyJob", RotateProfileKeyJob.KEY);
    put("RotateSignedPreKeyJob", RotateSignedPreKeyJob.KEY);
    put("SendDeliveryReceiptJob", SendDeliveryReceiptJob.KEY);
    put("SendReadReceiptJob", SendReadReceiptJob.KEY);
    put("ServiceOutageDetectionJob", ServiceOutageDetectionJob.KEY);
    put("SmsReceiveJob", SmsReceiveJob.KEY);
    put("SmsSendJob", SmsSendJob.KEY);
    put("SmsSentJob", SmsSentJob.KEY);
    put("TrimThreadJob", TrimThreadJob.KEY);
    put("TypingSendJob", TypingSendJob.KEY);
    put("UpdateApkJob", UpdateApkJob.KEY);
  }};

  public static @Nullable String getFactoryKey(@NonNull String workManagerClass) {
    return FACTORY_MAP.get(workManagerClass);
  }
}
