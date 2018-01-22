package august.lesincs.simplefilmreview.appinfo

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.widget.Toast
import august.lesincs.simplefilmreview.R

/**
 * Created by Administrator on 2017/9/4.
 */
class InfoFrag : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.frag_info)
        findPreference("GMail").setOnPreferenceClickListener {
            try {
                val uri = Uri.parse(getString(R.string.mail_to_GMail))
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject))
                intent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.build_model) + Build.MODEL + "\n"
                                + getString(R.string.build_version) + Build.VERSION.RELEASE + "\n")
                startActivity(intent)
            } catch (exception: android.content.ActivityNotFoundException) {
                toastMsg(R.string.no_mail_app)
            }
            true
        }
        findPreference("QQMail").setOnPreferenceClickListener {

            try {
                val uri = Uri.parse(getString(R.string.mail_to_QQMail))
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject))
                intent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.build_model) + Build.MODEL + "\n"
                                + getString(R.string.build_version) + Build.VERSION.RELEASE + "\n")
                startActivity(intent)
            } catch (exception: android.content.ActivityNotFoundException) {
                toastMsg(R.string.no_mail_app)
            }
            true
        }
        findPreference("open_source").setOnPreferenceClickListener {
            val url = getText(R.string.open_source_summary).toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            true
        }
        findPreference("github_address").setOnPreferenceClickListener {
            val url = getText(R.string.github_address_summary).toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            true
        }
    }

    private fun toastMsg(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()

}

