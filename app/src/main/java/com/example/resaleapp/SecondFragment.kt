package com.example.resaleapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.resaleapp.databinding.FragmentSecondBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val itemsViewModel: ItemViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Item Info"

        val bundle = requireArguments()
        val secondFragmentArgs: SecondFragmentArgs by navArgs()
        val item = secondFragmentArgs.item
        //val item = itemsViewModel[position]
        if (item == null) {
            binding.textviewMessage.text = "No such item!"
            return
        }
        binding.editTextTitle.setText("Title: "+item.title)
        binding.editTextDescription.setText("Description: "+item.description)
        binding.editTextPrice.setText("Price: "+item.price.toString())
        binding.editTextSeller.setText("Seller: "+item.seller)
        val format = SimpleDateFormat.getDateTimeInstance()
        val str = format.format(item.date * 1000L)
        binding.editTextDate.setText("Date: "+str)

        binding.buttonBack.setOnClickListener {
            // findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            // https://stackoverflow.com/questions/60003039/why-android-navigation-component-screen-not-go-back-to-previous-fragment-but-a-m
            findNavController().popBackStack()
        }

        binding.buttonDelete.setOnClickListener {
            if (Firebase.auth.currentUser != null) {
                if (auth.currentUser?.email == item.seller) {
                    itemsViewModel.delete(item.id)
                } else {
                    Snackbar.make(binding.root, "Not your item", Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(binding.root, "Sign in to delete", Snackbar.LENGTH_LONG).show()
            }
            findNavController().popBackStack()
        }

        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}