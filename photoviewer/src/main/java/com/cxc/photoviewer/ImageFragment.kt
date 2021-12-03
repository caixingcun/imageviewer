package com.cxc.photoviewer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher

/**
 * @author caixingcun
 * @date 2021/12/1
 * Description :
 */
class ImageFragment : Fragment() {
    companion object {
        const val PATH = "path"

        fun getInstance(path: String): ImageFragment {
            val imageFragment = ImageFragment()
            imageFragment.arguments = Bundle().apply {
                putString(PATH, path)
            }
            return imageFragment
        }
    }

    private var mAttacher: PhotoViewAttacher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var photoView: PhotoView = view.findViewById(R.id.photo_view)
        mAttacher = PhotoViewAttacher(photoView)
        var path = arguments?.getString(PATH)

        ImagesViewer.getInstance().imageEngine?.load(context, photoView, path)

        mAttacher?.setOnPhotoTapListener { _, _, _ ->
            activity?.let {
                val bigActivity = activity as BigImageActivity?
                bigActivity?.supportFinishAfterTransition()
            }
        }
    }

}