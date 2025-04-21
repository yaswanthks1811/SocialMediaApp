import React, { useState, useEffect, useRef } from 'react';
import { useSearchParams } from 'react-router-dom'; // If using React Router
import { viewMessages, sendMessage } from '../services/api'; // Adjust the path
import './ViewMessage.css';

import Navbar from '../components/NavBar';

function ViewMessages() {
  const [searchParams] = useSearchParams();
  // *** IMPORTANT: Rename search param to reflect the OTHER person's ID ***
  // It's confusing to call it 'senderId' here when it represents the *other* user
  // Let's call it 'otherUserId' or 'chatPartnerId' for clarity. Assuming 'senderId'
  // in the URL actually means the ID of the person you are chatting *with*.
  const otherUserId = searchParams.get('senderId');
  const [messages, setMessages] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [newMessage, setNewMessage] = useState('');
  const messagesEndRef = useRef(null); // Ref for scrolling
  const [chatPartnerName, setChatPartnerName] = useState('')

  // Function to scroll to the bottom
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    const fetchMessages = async () => {
      if (!otherUserId) {
        setError("No chat selected.");
        setLoading(false);
        setMessages([]);
        return;
      }
      setLoading(true);
      const result = await viewMessages(otherUserId); // Pass the ID of the other user
      if (result) {
        // Assuming the response you provided is directly in result.data
        // Make sure your API returns the correct structure
        // console.log(result.data);
        setChatPartnerName(result.data[0]); // The username is the first element
        // console.log(result.data[0]);
        // console.log(result[1]?.messages)
        setMessages(result.data[1]?.messages || []); // Messages are in the 'messages' property of the second element
        setError(null);
      } else {
        setError(result.error || "Failed to load messages.");
        setMessages([]);
      }
      setLoading(false);
    };

    fetchMessages();
  }, [otherUserId]); // Dependency is the ID of the person you're chatting with

  // Scroll to bottom when messages change
  useEffect(() => {
    scrollToBottom();
  }, [messages]);


  const handleSendMessage = async () => {
        if (!newMessage.trim() || !otherUserId) return;
    
        try {
          setNewMessage(''); // Clear input immediately
    
          const sendResult = await sendMessage(otherUserId, newMessage);
    
          if (!sendResult.success) {
              console.error('Send message error:', sendResult.error);
              setError('Failed to send message.');
              // Refetch messages on error to ensure UI is up-to-date
              const fetchResult = await viewMessages(otherUserId);
              if (fetchResult?.data && Array.isArray(fetchResult.data) && fetchResult.data.length >= 2) {
                setMessages(fetchResult.data[1]?.messages || []);
              } else {
                setError(fetchResult?.error || "Failed to refresh messages after send error.");
              }
          } else {
             // If send was successful, refetch messages
             const fetchResult = await viewMessages(otherUserId);
             if (fetchResult?.data && Array.isArray(fetchResult.data) && fetchResult.data.length >= 2) {
                setChatPartnerName(fetchResult.data[0]);
                setMessages(fetchResult.data[1]?.messages || []);
                setError(null); // Clear previous errors
             } else {
                setError(fetchResult?.error || "Failed to refresh messages after sending.");
             }
          }
        } catch (err) {
          console.error('Send message error:', err);
          setError('An unexpected error occurred while sending the message.');
        }
    };

   // Handle Enter key press in input
   const handleKeyPress = (event) => {
    if (event.key === 'Enter' && !event.shiftKey) { // Send on Enter, allow Shift+Enter for newline
      event.preventDefault(); // Prevent default form submission/newline
      handleSendMessage();
    }
  };

  if (loading) {
    return <p className="loading-text">Loading messages...</p>;
  }

  if (error && messages.length === 0) { // Show full page error only if no messages loaded
    return <p className="error-text">Error: {error}</p>;
  }

  return (
    <div className="view-messages-container">
      {/* Display the chat partner's ID or ideally their username if available */}
      <h2 className="view-messages-heading">Chat with {chatPartnerName}</h2>

      {/* Display error above messages if messages are also present */}
       {error && <p className="error-text" style={{ padding: '5px 15px', borderBottom: '1px solid #eee' }}>Error: {error}</p>}


      <div className="messages-list">
        {messages.length === 0 && !loading && !error && (
            <p className="loading-text">No messages yet. Start the conversation!</p>
        )}
        {messages.map((item) => {
          // Determine if the message was sent by the current user or received
          // **Crucial assumption:** The `viewMessages(otherUserId)` API call returns messages
          // where `item.message.senderId` is the person who SENT it.
          // We need the *current logged-in user's ID* to compare.
          // Let's assume you have access to it, e.g., `currentUser.id`
          const CURRENT_USER_ID = localStorage.getItem("userId"); // <-- REPLACE THIS WITH ACTUAL LOGGED-IN USER ID

          // If the sender of the message is the current logged-in user, it's 'sent'
          const isSent = item.message.senderId === CURRENT_USER_ID;

          // If the receiver of the message is the current logged-in user, it's 'received'
          // Note: Depending on your API, you might only need one check.
          // If your API for viewMessages(otherUserId) returns *all* messages between
          // the current user and otherUserId, then checking senderId is sufficient.
          // const isReceived = item.message.receiverId === CURRENT_USER_ID;


          return (
            <div
              key={item.message.messageId}
              // Apply 'message-sent' or 'message-received' class based on the check
              className={`message-item ${isSent ? 'message-sent' : 'message-received'}`}
            >
              <div className="message-content">
                 {/* Show sender username only for received messages */}
                 {!isSent && <strong>{item.senderUsername}:</strong>}
                 {item.message.messageContent}
              </div>
              <small className="message-timestamp">
                {new Date(item.message.createdAt).toLocaleString()}
              </small>
            </div>
          );
        })}
        {/* Empty div to mark the end of messages for scrolling */}
        <div ref={messagesEndRef} />
      </div>

      <div className="message-input-area">
        <input
          className="message-input"
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          onKeyPress={handleKeyPress} // Add key press handler
          placeholder="Type your message..."
          disabled={!otherUserId} // Disable if no chat is selected
        />
        <button
         className="send-button"
         onClick={handleSendMessage}
         disabled={!newMessage.trim() || !otherUserId} // Disable if no text or no chat selected
        >
            Send
        </button>
      </div>
      <Navbar className="bottom-navbar" />
    </div>
  );
}

export default ViewMessages;