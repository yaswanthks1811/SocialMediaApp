import React, { useState } from 'react';
import { uploadPost } from '../services/api';
import { useNavigate } from 'react-router-dom';
import './CreatePost.css';
import Navbar from '../components/NavBar';

function CreatePost() {
    const [caption, setCaption] = useState('');
    const [postType, setPostType] = useState('Image');
    const [image, setImage] = useState(null);
    const [video, setVideo] = useState(null);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleImageChange = (e) => {
        setImage(e.target.files[0]);
        setVideo(null);
    };

    const handleVideoChange = (e) => {
        setVideo(e.target.files[0]);
        setImage(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(false);
        setLoading(true);

        const postData = {
            caption,
            postType,
            image,
            video,
        };

        try {
            const result = await uploadPost(postData);
            if (result.success) {
                setSuccess(true);
                setCaption('');
                setImage(null);
                setVideo(null);
                setTimeout(() => navigate("/profile"), 1500);
            } else {
                setError(result.error);
            }
        } catch (err) {
            setError('Failed to upload post. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="create-post-container">
            <h2 className="create-post-title">Create Post</h2>
            
            {error && <div className="error-message">{error}</div>}
            {success && <div className="success-message">Post uploaded successfully!</div>}

            <form onSubmit={handleSubmit} className="post-form">
                <div className="form-group">
                    <label className="form-label">
                        Caption:
                        <textarea 
                            value={caption} 
                            onChange={(e) => setCaption(e.target.value)}
                            className="caption-input"
                            placeholder="What's on your mind?"
                        />
                    </label>
                </div>

                <div className="form-group">
                    <label className="form-label">
                        Post Type:
                        <select 
                            value={postType} 
                            onChange={(e) => setPostType(e.target.value)}
                            className="type-select"
                        >
                            <option value="Image">Image</option>
                            <option value="Video">Video</option>
                        </select>
                    </label>
                </div>

                <div className="form-group">
                    {postType === 'Image' ? (
                        <label className="file-upload-label">
                            <span>Select Image:</span>
                            <input 
                                type="file" 
                                accept="image/*" 
                                onChange={handleImageChange}
                                className="file-input"
                            />
                            {image && (
                                <div className="file-selected">
                                    Selected: {image.name}
                                </div>
                            )}
                        </label>
                    ) : (
                        <label className="file-upload-label">
                            <span>Select Video:</span>
                            <input 
                                type="file" 
                                accept="video/*" 
                                onChange={handleVideoChange}
                                className="file-input"
                            />
                            {video && (
                                <div className="file-selected">
                                    Selected: {video.name}
                                </div>
                            )}
                        </label>
                    )}
                </div>

                <button 
                    type="submit" 
                    className="submit-button"
                    disabled={loading || (!image && !video)}
                >
                    {loading ? 'Uploading...' : 'Upload Post'}
                </button>
            </form>
            <Navbar className="bottom-navbar" />
        </div>
    );
}

export default CreatePost;