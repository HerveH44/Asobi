import React from "react";

import Header from "./Header";
import JoinPanel from "./JoinPanel";
import NewsPanel from "./NewsPanel";
import Footer from "./Footer";
import CreatePanel from "./CreatePanel";

const Lobby = () => (
  <div className="container">
    <div className="lobby">
      <Header/>
      <CreatePanel/>
      <JoinPanel/>
      <NewsPanel/>
      <Footer/>
    </div>
  </div>
);

export default Lobby;
