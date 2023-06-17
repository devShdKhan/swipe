package com.example.swipeassignment.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.swipeassignment.R
import com.example.swipeassignment.databinding.FragmentAddProductBinding
import com.example.swipeassignment.model.AddProductResponse
import com.example.swipeassignment.model.Product
import com.example.swipeassignment.model.Resource
import com.example.swipeassignment.utils.toSafeDouble
import com.example.swipeassignment.viewmodel.AddProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AddProductFragment : Fragment(), View.OnClickListener {

    private val viewModel by viewModels<AddProductViewModel>()
    private lateinit var binding: FragmentAddProductBinding
    private var imageUri: Uri? = null

    private val imagePicker = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri == null) {
            Toast.makeText(context, getString(R.string.no_image_selected), Toast.LENGTH_SHORT)
                .show()
            return@registerForActivityResult
        }
        setImage(uri)
    }

    private var selectedProductType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        lifecycleScope.launch {
            viewModel.addProductFlow.collect {
                when (it) {
                    is Resource.Loading -> binding.progress.visibility = View.VISIBLE
                    is Resource.Success -> handleSuccess(it.data)
                    is Resource.Error -> handleError(it.message)
                }
            }
        }

    }

    private fun initViews() {
        binding.imgProduct.setOnClickListener(this)
        binding.imgClear.setOnClickListener(this)
        binding.btnSaveProduct.setOnClickListener(this)

        val productTypes = resources.getStringArray(R.array.product_types)

        binding.spProductType.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, productTypes
        )

        binding.spProductType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedProductType = if (position != 0) productTypes[position] else ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedProductType = ""
            }
        }
    }

    private fun handleError(message: String) {
        binding.btnSaveProduct.isEnabled = true
        binding.progress.visibility = View.GONE
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccess(response: AddProductResponse) {
        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imgProduct -> {
                imagePicker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            }

            binding.imgClear -> {
                imageUri = null
                binding.imgProduct.setImageResource(R.drawable.product_placeholder)
                binding.imgClear.visibility = View.GONE
            }

            binding.btnSaveProduct -> {
                v.isEnabled = false
                val product = Product(
                    product_name = binding.etProductName.text.toString(),
                    product_type = selectedProductType,
                    price = binding.etProductPrice.text.toString().toSafeDouble(),
                    tax = binding.etProductTax.text.toString().toSafeDouble(),
                    image = ""
                )
                viewModel.saveProduct(product, convertUriToFile(imageUri))
            }
        }
    }

    private fun setImage(uri: Uri) {
        imageUri = uri
        binding.imgProduct.setImageURI(uri)
        binding.imgClear.visibility = View.VISIBLE
    }

    private fun convertUriToFile(uri: Uri?): File? {
        uri?.let {
            val fileDir = requireContext().applicationContext.filesDir
            val file = File(fileDir, "image.jpg")
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            return file
        } ?: return null
    }
}