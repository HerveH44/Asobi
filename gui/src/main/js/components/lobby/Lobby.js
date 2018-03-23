import React, { Fragment } from "react";
import {object, func} from "prop-types";

import Header from "./Header";
import JoinPanel from "./JoinPanel";
import NewsPanel from "./NewsPanel";
import Footer from "./Footer";
import CreatePanel from "./CreatePanel";

const Lobby = ({site, game, editGame}) => (
  <Fragment>
      <Header {...site}/>
      <CreatePanel {...game} editGame={editGame} />
      <JoinPanel {...site}/>
      <NewsPanel {...site}/>
      <Footer {...site}/>
  </Fragment>
);

Lobby.propTypes = {
  site: object.isRequired,
  game: object.isRequired,
  editGame: func.isRequired
}

export default Lobby;
