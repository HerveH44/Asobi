import React from "react";
import Log from "./Log";
import Download from "./Download";
import Copy from "./Copy";

const DownloadPanel = () => (
    <fieldset className='download-controls fieldset'>
        <legend className='legend game-legend'>Download</legend>
        <Download/>
        <Copy/>
        <Log />
    </fieldset>
);

export default DownloadPanel;
