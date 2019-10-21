import React, {Component, Fragment} from 'react';
import logo from './resources/mars_rover.jpeg';
import './App.css';

// const URL = "http://localhost:8080/api/";
const URL = "/api/";
const downloadPhotos = URL + "photos/download";
const getMorePhotos = URL + "photos/more";
const getAllPhotos = URL + "photos/update";
const deletePhotos = URL + "photos/delete";

class App extends Component {

    state = {
        images: [],
        loading: true
    };

    componentDidMount() {
        // console.log("Mounted!");
        this.callAPI(getAllPhotos);
    }

    componentDidUpdate() {
        // console.log("Updated!", this.state.images);
    }

    callAPI(endpoint) {
        console.log("calling endpoint " + endpoint);
        this.setState({loading: true});

        fetch(endpoint)
            .then(res => res.json())
            .then((data) => {
                console.log(data);
                if (data && data.constructor === Array) {
                    console.log("setting Data", data);
                    this.setState({
                        images: data,
                        loading: false
                    });
                } else if (data === true) {
                    this.callAPI(getAllPhotos);
                }
            })
            .catch(console.log)
    };

    handleClick(e, endpoint) {
        e.preventDefault();
        console.log('endpoint - %s - clicked', endpoint);
        this.callAPI(endpoint);
    };

    generateLink(text, apiCall) {
        return (
            // eslint-disable-next-line
            <a className="App-link" onClick={e => this.handleClick(e, apiCall)}>{text}</a>
        )
    }

    generatePhotos() {
        const {images} = this.state;
        console.log("generate photos - ", images);

        const showImage = (image) => {
            if (image.localFilePath === "") {
                return (<p>No Images found for date: {image.date}</p>);
            }

            const img = image.localFilePath === null ?
                (<p>Images not loaded yet, click load...</p>) :
                (<img className="image-size" src={image.localFilePath} alt="info"/>);

            const info = image.date === null ?
                (<li>No info for this image</li>) :
                (<Fragment>
                    <li>Date: {image.date}</li>
                    <li>Rover: {image.roverName}</li>
                    <li>Camera: {image.camera}</li>
                </Fragment>);

            return (
                    <div>
                        <ul className="list-details">
                            {info}
                        </ul>
                        {img}
                    </div>
                );
        };

        return (
            <div className="images-list">
                {images.map(
                    (image, index) =>
                        <div className="inner-image" key={index}>
                            {showImage(image)}
                        </div>
                )}
            </div>
        )
    }

    render() {
        console.log("loading? - ",this.state.loading);
        const loadingOrLinks = this.state.loading ?
            (<div>Loading....</div>) :
            (<Fragment>
                    {this.generateLink("Load Random Images for Default Dates - " + this.state.images.length, downloadPhotos)}
                    {this.generateLink("Get More Random Images for Default Dates", getMorePhotos)}
                    {this.generateLink("Remove Images", deletePhotos)}
                    {this.generateLink("Update Images", getAllPhotos)}
            </Fragment>);

        return (
            <div className="App">
                <header className="App-header">
                    <p>
                        Load random images from the Mars Rover for given dates!
                    </p>
                    <img src={logo} className="App-logo" alt="logo"/>
                    {loadingOrLinks}
                    {this.generatePhotos()}
                </header>
            </div>
        )
    }
}

export default App;
