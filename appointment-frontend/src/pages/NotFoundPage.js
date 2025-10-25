import React from "react";
import { Link } from "react-router-dom";

const NotFoundPage = () => {
  return (
    <div className="flex flex-col gap-2">
      404 NOT FOUND
      <Link to="/">Ana sayfaya geri dönün</Link>
    </div>
  );
};

export default NotFoundPage;
