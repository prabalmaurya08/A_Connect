package com.example.a_connect.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_connect.databinding.FragmentAlumniLoginBinding


class AlumniLogin : Fragment() {
    private lateinit var binding: FragmentAlumniLoginBinding
     private var listener: OnAlumniScreenClicked?=null




    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnAlumniScreenClicked -> {
                listener=context


            }



            else -> {
                throw ClassCastException("$context must implement OnSignupClickListener")
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAlumniLoginBinding.inflate(layoutInflater)

        setup()
        return binding.root
    }

    private fun setup() {
        binding.submitButton.setOnClickListener {
            listener?.onAlumniSubmitClicked()
        }
        binding.aluminiLoginAsAdmin.setOnClickListener {
            listener?.onAlumniAdminClicked()
        }
    }




//                    MAKING THIS INTERFACE TO USE COMPONENT INSIDE THE VIEWPAGER
    interface OnAlumniScreenClicked{

        fun onAlumniSubmitClicked()
        fun onAlumniAdminClicked()

    }


}