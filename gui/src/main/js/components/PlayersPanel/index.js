import React from "react";

import PlayersEntries from "./PlayersEntries"

const PlayersPanel = () => (
  <fieldset className='fieldset'>
    <legend className='legend game-legend'>Players</legend>
    <PlayersTable />
  </fieldset>
);

const PlayersTable = () => (
  <table id='players'>
    <tbody>
      <PlayersTableHeader />
      <PlayersEntries />
    </tbody>
  </table>
);

const PlayersTableHeader = () => (
  <tr>
    <th key="1">#</th>
    <th key="2"/>
    <th key="3">name</th>
    <th key="4">packs</th>
    <th key="7">time</th>
    <th key="8">cock</th>
    <th key="9">mws</th>
  </tr>
);

export default PlayersPanel;
