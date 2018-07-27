import React from "react";
import {func, string, array} from "prop-types";
import {connect} from "react-redux";
import {onChangeFileName, onChangeFileType, onClickDownload} from "../../../actions/game";
import {Select} from "../../utils";
import {downloadDeck} from "../../../reducers/playerState";

const Download = ({fileType, onChangeFileType, fileTypes, onClickDownload, fileName, onChangeFileName}) => (
    <div className='connected-container'>
        <button className='connected-component' onClick={onClickDownload}>
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
};

const mapStateToProps = ({playerState, gameSettings: {fileType, fileTypes, fileName}}) => ({
    fileType, fileTypes, fileName,
    onClickDownload: downloadDeck(playerState, fileName, fileType)
});

const mapDispatchToProps = {
    onChangeFileType, onChangeFileName
};

export default connect(mapStateToProps, mapDispatchToProps)(Download);
