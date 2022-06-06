package com.example.resaleapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resaleapp.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val itemsViewModel: ItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Resale Items"

        /*binding.fab.setOnClickListener { view ->
            showDialog()
        }*/

        if (auth.currentUser != null) {
            binding.buttonSignin.visibility = View.GONE
        } else {
            binding.buttonSignin.visibility = View.VISIBLE
        }

        binding.buttonSignin.setOnClickListener { view ->
            if (auth.currentUser == null) {
                findNavController().navigate(R.id.action_FirstFragment_to_LoginFragment)
            } else {
                Snackbar.make(binding.root, "You are already signed in", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.buttonSortprice.setOnClickListener { view ->
            itemsViewModel.sortByPrice()
        }

        binding.buttonSortdate.setOnClickListener { view ->
            itemsViewModel.sortByDate()
        }

        binding.buttonFilter.setOnClickListener { view ->
            val text1 = binding.filterMaxprice.text.toString().trim()
            val nr1 = text1.toInt()
            itemsViewModel.filterMaxPrice(nr1)
        }

        itemsViewModel.itemsLiveData.observe(viewLifecycleOwner) { items ->
            //Log.d("APPLE", "observer $items")
            binding.progressbar.visibility = View.GONE
            binding.recyclerView.visibility = if (items == null) View.GONE else View.VISIBLE
            if (items != null) {
                val adapter = MyAdapter(items) { position ->
                    val item: Item? = itemsViewModel[position]
                    val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(item!!)
                    findNavController().navigate(action)
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                binding.recyclerView.adapter = adapter
            }
        }

        itemsViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textviewMessage.text = errorMessage
        }

        itemsViewModel.reload()

        binding.swiperefresh.setOnRefreshListener {
            itemsViewModel.reload()
            binding.swiperefresh.isRefreshing = false
        }

        /* binding.buttonFirst.setOnClickListener {
             findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
         }*/
    }

    /*private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add item")

        val layout = LinearLayout(this@FirstFragment)
        layout.orientation = LinearLayout.VERTICAL

        val titleInputField = EditText(this)
        titleInputField.hint = "Title"
        titleInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(titleInputField)

        val descriptionInputField = EditText(this)
        descriptionInputField.hint = "Description"
        descriptionInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(descriptionInputField)

        val priceInputField = EditText(this)
        priceInputField.hint = "Price"
        priceInputField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_NUMBER
        layout.addView(priceInputField)

        val sellerInputField = EditText(this)
        sellerInputField.hint = "Seller"
        sellerInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(sellerInputField)

        val dateInputField = EditText(this)
        dateInputField.hint = "Date"
        dateInputField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_NUMBER
        layout.addView(dateInputField)

        val pictureUrlInputField = EditText(this)
        pictureUrlInputField.hint = "Picture URL"
        pictureUrlInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(pictureUrlInputField)

        builder.setView(layout)

        builder.setPositiveButton("OK") { dialog, which ->
            val title = titleInputField.text.toString().trim()
            val description = descriptionInputField.text.toString().trim()
            val priceStr = priceInputField.text.toString().trim()
            val seller = sellerInputField.text.toString().trim()
            val dateStr = dateInputField.text.toString().trim()
            val pictureUrl = pictureUrlInputField.text.toString().trim()
            when {
                title.isEmpty() ->
                    //inputField.error = "No word"
                    Snackbar.make(binding.root, "No title", Snackbar.LENGTH_LONG).show()
                title.isEmpty() -> Snackbar.make(binding.root, "No title", Snackbar.LENGTH_LONG)
                    .show()
                priceStr.isEmpty() -> Snackbar.make(
                    binding.root,
                    "No price",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                else -> {
                    val item = Item(title, description, priceStr.toInt(), seller, dateStr.toInt(), pictureUrl)
                    itemsViewModel.add(item)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}