package my.com.fashionapp.UI

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.ClaimViewModel
import my.com.fashionapp.data.RewardViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentRewardDetailBinding
import my.com.fashionapp.util.errorDialog
import my.com.fashionapp.util.generateQRCode
import my.com.fashionapp.util.toBitmap
import my.com.fashionappstaff.data.ClaimHistory
import my.com.fashionappstaff.data.Reward
import my.com.fashionappstaff.data.User


class RewardDetailFragment : Fragment() {

    private lateinit var binding: FragmentRewardDetailBinding
    private val nav by lazy{ findNavController() }
    private val vmU : UserViewModel by activityViewModels()
    private val vmR : RewardViewModel by activityViewModels()
    private val vmCh : ClaimViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id", "N/A") }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRewardDetailBinding.inflate(inflater, container, false)

        // TODO
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        detail()


        binding.imgRewardDetailBack.setOnClickListener { nav.navigateUp() }
        binding.btnRewardDetialClaim.setOnClickListener { claim() }
        binding.btnSizeXS.setOnClickListener {
            binding.txtSize.text = binding.btnSizeXS.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
        }

        binding.btnSizeS.setOnClickListener {
            binding.txtSize.text = binding.btnSizeS.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
        }

        binding.btnSizeM.setOnClickListener {
            binding.txtSize.text = binding.btnSizeM.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
        }

        binding.btnSizeL.setOnClickListener {
            binding.txtSize.text = binding.btnSizeL.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
        }

        binding.btnSizeXL.setOnClickListener {
            binding.txtSize.text = binding.btnSizeXL.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
        }


        return binding.root
    }

    private fun detail() {

        val r = vmR.get(id)
        if (r == null){
            nav.navigateUp()
            return
        }

        binding.imgRewardDetailPic.setImageBitmap(r.rewardPhoto.toBitmap())
        binding.txtRewardDetailName.setText(r.rewardName)
        binding.txtRewardDetailPoint.setText("%.2f".format(r.rewardPoint))
        binding.txtRewardDetailDescription.setText(r.rewardDescrip)

    }

    private fun claim() {

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val r = vmR.get(id)
        val u = emailLogin?.let { vmU.getEmail(it) }
        val userName = u?.userName


        if (r?.rewardQuan != 0){

            if(u!!.userPoint >= r!!.rewardPoint){

                if (binding.txtSize.text != "" ){
                    var chkID = vmCh.validID()
                    val quan = r.rewardQuan - 1
                    val uPoint = u.userPoint
                    val rPoint = r.rewardPoint

                    val totalPoint = uPoint - rPoint

                    val r = Reward(
                        rewardID  = r.rewardID,
                        rewardName    = r.rewardName,
                        rewardDescrip  = r.rewardDescrip,
                        rewardQuan    = quan,
                        rewardPoint   = r.rewardPoint,
                        expiryDate    = r.expiryDate,
                        rewardPhoto   = r.rewardPhoto,
                    )

                    val ch = ClaimHistory(
                        claimHistoryID =  chkID,
                        claimRewardName = r.rewardName,
                        claimRewardQuantity = 1,
                        claimRewardPoint = r.rewardPoint,
                        claimRewardImage = r.rewardPhoto,
                        username  = u.userName,
                    )

                    val u = User(
                        userId = u.userId,
                        email = u.email,
                        password = u.password,
                        userName = u.userName,
                        phoneNumber = u.phoneNumber,
                        userPhoto = u.userPhoto,
                        homeAddress = u.homeAddress,
                        userPoint = totalPoint,
                        userType = u.userType
                    )

                    inform("Are you sure want to claim this reward ? ", ch, u, r)


                }else{
                    val err = " Select the Size of Reward. \n"
                    errorDialog(err)
                }
            } else {
                var err = " Not Enough Point. \n"
                errorDialog(err)
            }
        } else {
            val err = " Out Of Stock\n"
            errorDialog(err)
        }


    }

    fun Fragment.inform(text: String, ch: ClaimHistory, u: User, r: Reward) {
        AlertDialog.Builder(context)
            .setTitle("Information")
            .setMessage(text)
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                vmCh.set(ch)
                vmU.set(u)
                vmR.set(r)
                nav.navigate(R.id.rewardFragment)
                dialog.cancel()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            .show()
    }

}