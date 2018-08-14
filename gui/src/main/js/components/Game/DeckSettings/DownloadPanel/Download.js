import React from "react";
import {func, string, array} from "prop-types";
import {connect} from "react-redux";
import {hashDeck, onChangeFileName, onChangeFileType, onClickDownload} from "../../../../actions/game";
import {Select} from "../../../utils";
import {downloadDeck} from "../../../../reducers/playerState";

const Download = ({fileType, onChangeFileType, fileTypes, onClickDownload, fileName, onChangeFileName, hashDeck}) => (
    <div className='connected-container'>
        <button className='connected-component' onClick={() => {hashDeck(); onClickDownload();}}>
            Download as
        </button>
        <input
            type='text'
            className='download-filename connected-component'
            placeholder='filename'
            value={fileName}
            onChange={onChangeFileName}/>
        <Select value={fileType} onChange={onChangeFileType} opts={fileTypes}/>
        <span className='download-button'/>
    </div>
);

Download.propTypes = {
    fileType: string.isRequired,
    onChangeFileType: func.isRequired,
    fileTypes: array.isRequired,
    onClickDownload: func.isRequired,
    fileName: string.isRequired,
    onChangeFileName: func.isRequired,
    hashDeck: func.isRequired,
};

const mapStateToProps = ({playerState, gameSettings: {fileType, fileTypes, fileName}}) => ({
    fileType, fileTypes, fileName,
    onClickDownload: downloadDeck(playerState, fileName, fileType)
});

const mapDispatchToProps = {
    onChangeFileType, onChangeFileName, hashDeck
};

export default connect(mapStateToProps, mapDispatchToProps)(Download);
