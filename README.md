## Loquacious - Getting resources on demand

[![CircleCI](https://circleci.com/gh/saantiaguilera/android-api-loquacious/tree/master.svg?style=svg)](https://circleci.com/gh/saantiaguilera/android-api-loquacious/tree/master)

Loquacious is a library for saving resources (Strings, booleans, whatever) in a localized way.

My main objective (please know that this is a proof of concept) is to try to reduce the APK size more
and more. The aim of this library is:
- Having your own server that will have your resources (and you will query based on the locale the user has setted / changed)
- Having empty resources in your APK, not separated by different archs/densities/locales/whatever. This way:
  * We reduce the .arsc table, as there are not repetitions nor empty trashings
  * We reduce the resources files, since they are all empty (and will be queried to your back end)
  * The developer still feels and interacts with the system as usual

## Usage

TBD

## License

BSD License

Copyright (c) 2017-present. All rights reserved.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.