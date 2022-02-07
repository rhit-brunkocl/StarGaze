package edu.rosehulman.stargaze.ui.Camera

import android.app.ActionBar
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import edu.rosehulman.stargaze.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    lateinit var binding: FragmentCameraBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)



        return binding.root
    }


}