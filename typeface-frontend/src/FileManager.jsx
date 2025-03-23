import { useState, useEffect, useRef } from "react";
import { FiDownload, FiTrash2 } from "react-icons/fi";
import "./FileManager.css";

const API_BASE_URL = "http://localhost:8080/api";

export default function FileManager() {
  const [files, setFiles] = useState([]);
  const fileInputRef = useRef(null);

  useEffect(() => {
    fetchFiles();
  }, []);

  const fetchFiles = async () => {
    const response = await fetch(`${API_BASE_URL}/files`);
    const data = await response.json();
    setFiles(data);
  };

  const handleFileUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    await fetch(`${API_BASE_URL}/upload`, {
      method: "POST",
      body: formData,
    });

    // Reset file input field
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }

    fetchFiles();
  };

  const handleDownload = async (fileName) => {
    const response = await fetch(`${API_BASE_URL}/download?fileName=${fileName}`, {
      method: "POST" 
    });
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  };

  const handleDelete = async (fileName) => {
    await fetch(`${API_BASE_URL}/delete?fileName=${fileName}`, {
      method: "DELETE",
    });
    fetchFiles();
  };

  return (
    <div className="container">
      <div className="file-manager">
        <h1 className="title">File Manager</h1>

        {/* Upload Button */}
        <input type="file" ref={fileInputRef} onChange={handleFileUpload} className="file-input" />

        {/* File List */}
        <ul className="file-list">
          {files.map((file) => (
            <li key={file.fileName} className="file-item">
              <span className="file-info">
                {file.fileName} ({(file.fileSize / 1024).toFixed(2)} KB)
              </span>
              <div>
                <button onClick={() => handleDownload(file.fileName)} className="download-btn">
                    <FiDownload />
                </button>
                <button
                  onClick={() => handleDelete(file.fileName)}className="delete-btn">
                  <FiTrash2 />
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
